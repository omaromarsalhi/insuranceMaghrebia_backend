package com.maghrebia.quotegenerator.service;

import com.maghrebia.quotegenerator.dto.*;
import com.maghrebia.quotegenerator.exception.InvalidDataException;
import com.maghrebia.quotegenerator.model.VehicleInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AutomobileQuoteService {

    private final VINDecoderService vinDecoderService;
    private final WeatherService weatherService;

    public QuoteResponse calculate(AutoInsuranceRequest autoInsuranceRequest) {

        float annualPremium = calculateAnnualPremium(autoInsuranceRequest);

        float periodPremium = convertToBillingPeriod(
                annualPremium,
                autoInsuranceRequest.billingPeriod()
        );

        return QuoteResponse.builder()
                .baseAnnualPremium(annualPremium)
                .amount(periodPremium)
                .currency("TND")
                .billingPeriod(autoInsuranceRequest.billingPeriod())
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusYears(1))
                .build();
    }

    public GeoWeatherData getGeoWeatherData(AddressInfo addressInfo){

        WeatherData weatherData = weatherService.getWeatherInfo(addressInfo.boundingBox().northEast().lat(),
                addressInfo.boundingBox().northEast().lng());

        FilteredWeatherdata filteredWeatherdata = weatherService.getAverages(weatherData.daily());
        return GeoWeatherData.builder()
                .isUrbanArea(isUrbanArea(addressInfo))
                .averageMaxTmp(filteredWeatherdata.averageMaxTmp())
                .averageMinTmp(filteredWeatherdata.averageMinTmp())
                .averagePrecipitation(filteredWeatherdata.averagePrecipitation())
                .averageWind(filteredWeatherdata.averageWind())
                .build();
    }


    private float convertToBillingPeriod(float annualPremium, BillingPeriod period) {
        return switch (period) {
            case MONTHLY -> annualPremium / 12;
            case QUARTERLY -> annualPremium / 4;
            case SEMI_ANNUAL -> annualPremium / 2;
            case ANNUAL -> annualPremium;
        };
    }

    private float calculateAnnualPremium(AutoInsuranceRequest autoInsuranceRequest) {

        VehicleInfo vehicleInfo = vinDecoderService.getVehicleInfo(autoInsuranceRequest.vin());

        verifyVin(vehicleInfo, autoInsuranceRequest);

        WeatherData weatherData = weatherService.getWeatherInfo(autoInsuranceRequest.addressInfo().boundingBox().northEast().lat(),
                autoInsuranceRequest.addressInfo().boundingBox().northEast().lng());

        FilteredWeatherdata filteredWeatherdata = weatherService.getAverages(weatherData.daily());

        float basePremium = calculateBasePremium(vehicleInfo);

        float riskFactor = 1.0f;

        riskFactor += calculateDriverRisk(autoInsuranceRequest);

        riskFactor += calculateGeographicRisk(autoInsuranceRequest);

        riskFactor += calculateVehicleRisk(vehicleInfo);

        riskFactor += calculateWeatherRisk(weatherData.daily(), filteredWeatherdata);

        float coverageMultiplier = getCoverageMultiplier(autoInsuranceRequest.coverageType());


        return basePremium * riskFactor * coverageMultiplier;
    }

    public float calculateWeatherRisk(WeatherData.DailyData rawWeather, FilteredWeatherdata filteredWeather) {

        float weatherRisk = 0f;

        if (filteredWeather.averagePrecipitation() > 5.0f) {
            weatherRisk += 0.15f;
        }

        float tempVariation = (float) (filteredWeather.averageMaxTmp() - filteredWeather.averageMinTmp());
        weatherRisk += tempVariation * 0.02f;

        weatherRisk += calculateWindRisk(rawWeather, filteredWeather);

        return weatherRisk;
    }

    private float calculateWindRisk(WeatherData.DailyData daily, FilteredWeatherdata filtered) {
        float windRisk = 0f;

        windRisk += (float) (filtered.averageWind() * 0.01f);

        long highWindDays = daily.windspeed_10m_max().stream()
                .filter(Objects::nonNull)
                .filter(speed -> speed > 30.0f)
                .count();
        windRisk += highWindDays * 0.05f;

        return windRisk;
    }

    private float calculateDriverRisk(AutoInsuranceRequest request) {
        float risk = 0f;

        risk -= Math.min(request.drivingExperience() * 0.05f, 0.20f);

        if (request.accidentHistory().contains("+")) risk += 0.25f;
        else if (request.accidentHistory().contains("1 accidents")) risk += 0.2f;
        else if (!request.accidentHistory().equals("0 accidents")) risk += 0.15f;

        if (request.trafficViolations()) risk += 0.1f;

        if (request.defensiveDrivingCourse()) risk -= 0.15f;

        return risk;
    }

    private float calculateGeographicRisk(AutoInsuranceRequest request) {
        float risk = 0f;
        AddressInfo address = request.addressInfo();

        if (isUrbanArea(address)) {
            risk += 0.25f;
        } else {
            risk += 0.10f;
        }

        return risk;
    }

    private boolean isUrbanArea(AddressInfo address) {
        return !(address.streetName() == null && address.streetNumber() == null);
    }

    private float calculateBasePremium(VehicleInfo vehicle) {
        float base = switch (vehicle.getVehicleType().toLowerCase()) {
            case "car" -> 500.0f;
            case "motorcycle" -> 700.0f;
            case "truck" -> 900.0f;
            default -> 600.0f;
        };

        int vehicleAge = 2024 - Integer.parseInt(vehicle.getModelYear());
        base *= (1 - (vehicleAge * 0.015f));

        return base;
    }

    private float calculateVehicleRisk(VehicleInfo vehicleInfo) {
        float risk = 0f;

        int safetyFeatures = countSafetyFeatures(vehicleInfo);
        risk -= safetyFeatures * 0.04f;

        risk += calculateEngineRisk(vehicleInfo.getDisplacementL());

        risk += parseWeightClassRisk(vehicleInfo.getGrossVehicleWeightRating());

        risk += (Integer.parseInt(vehicleInfo.getSeatingCapacity()) - 2) * 0.02f;

        return Math.max(risk, 0.05f);
    }

    private int countSafetyFeatures(VehicleInfo vehicleInfo) {
        int count = 0;
        if (vehicleInfo.getAbs().equalsIgnoreCase("standard")) count++;
        if (vehicleInfo.getTractionControl().equalsIgnoreCase("standard")) count++;
        if (vehicleInfo.getElectronicStabilityControl().equalsIgnoreCase("standard")) count++;
        if (vehicleInfo.getBackupCamera().equalsIgnoreCase("standard")) count++;
        if (vehicleInfo.getDaytimeRunningLights().equalsIgnoreCase("standard")) count++;
        return count;
    }

    private float calculateEngineRisk(String displacement) {
        try {
            float engineSize = Float.parseFloat(displacement);
            return Math.min(engineSize * 0.03f, 0.15f);
        } catch (NumberFormatException e) {
            return 0.08f;
        }
    }

    private float parseWeightClassRisk(String gvwr) {
        if (gvwr.contains("6,000 lb")) return 0.12f;
        if (gvwr.contains("5,000 lb")) return 0.08f;
        return 0.05f;
    }

    private float getCoverageMultiplier(String coverageType) {
        return switch (coverageType.toLowerCase()) {
            case "comprehensive" -> 1.4f;
            case "third-party" -> 0.8f;
            default -> 1.0f;
        };
    }

    public void verifyVin(VehicleInfo vehicleInfo, AutoInsuranceRequest autoInsuranceRequest) {

        if (!vehicleInfo.getVehicleType().equalsIgnoreCase(autoInsuranceRequest.vehicleType()))
            throw new InvalidDataException("Vehicle Type does not match");

        if (!vehicleInfo.getMake().equalsIgnoreCase(autoInsuranceRequest.vehicleMake()))
            throw new InvalidDataException("Vehicle Make does not match");

        if (!vehicleInfo.getModel().equalsIgnoreCase(autoInsuranceRequest.vehicleModel()))
            throw new InvalidDataException("Vehicle Model does not match");

    }
}

