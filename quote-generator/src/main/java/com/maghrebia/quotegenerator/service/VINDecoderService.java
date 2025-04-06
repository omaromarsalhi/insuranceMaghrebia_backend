package com.maghrebia.quotegenerator.service;

import com.maghrebia.quotegenerator.model.VehicleInfo;
import com.maghrebia.quotegenerator.dto.NHTSAVinResponse;
import com.maghrebia.quotegenerator.dto.VinResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Service
public class VINDecoderService {

    private static final String NHTSA_API_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/decodevin/";

    private final RestTemplate restTemplate;

    public VINDecoderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VehicleInfo getVehicleInfo(String vin) {
        String url = NHTSA_API_URL + vin + "?format=json";

        ResponseEntity<NHTSAVinResponse> response = restTemplate.getForEntity(url, NHTSAVinResponse.class);

        return mapToVehicleInfo(response.getBody(), vin);
    }

    private VehicleInfo mapToVehicleInfo(NHTSAVinResponse apiResponse, String vin) {
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setVin(vin);

        Optional.ofNullable(apiResponse)
                .map(NHTSAVinResponse::getResults)
                .ifPresent(results -> {
                    // Core Identification
                    vehicleInfo.setMake(getValueByVariable(results, "Make"));
                    vehicleInfo.setModel(getValueByVariable(results, "Model"));
                    vehicleInfo.setModelYear(getValueByVariable(results, "Model Year"));
                    vehicleInfo.setVehicleType(getValueByVariable(results, "Vehicle Type"));

                    // Engine/Fuel
                    vehicleInfo.setFuelTypePrimary(getValueByVariable(results, "Fuel Type - Primary"));
                    vehicleInfo.setDisplacementL(getValueByVariable(results, "Displacement (L)"));

                    // Safety Features
                    vehicleInfo.setAbs(getValueByVariable(results, "Anti-lock Braking System (ABS)"));
                    vehicleInfo.setTractionControl(getValueByVariable(results, "Traction Control"));
                    vehicleInfo.setElectronicStabilityControl(getValueByVariable(results, "Electronic Stability Control (ESC)"));
                    vehicleInfo.setBackupCamera(getValueByVariable(results, "Backup Camera"));
                    vehicleInfo.setDaytimeRunningLights(getValueByVariable(results, "Daytime Running Light (DRL)"));

                    // Characteristics
                    vehicleInfo.setBodyClass(getValueByVariable(results, "Body Class"));
                    vehicleInfo.setDoors(getValueByVariable(results, "Doors"));
                    vehicleInfo.setSeatingCapacity(getValueByVariable(results, "Number of Seats"));
                    vehicleInfo.setGrossVehicleWeightRating(getValueByVariable(results, "Gross Vehicle Weight Rating From"));

                    // Manufacturing
                    vehicleInfo.setPlantCountry(getValueByVariable(results, "Plant Country"));
                });
        return vehicleInfo;
    }

    private String getValueByVariable(List<VinResult> results, String variableName) {
        return results.stream()
                .filter(r -> variableName.equals(r.getVariable()))
                .findFirst()
                .map(VinResult::getValue)
                .orElse(null);
    }
}

