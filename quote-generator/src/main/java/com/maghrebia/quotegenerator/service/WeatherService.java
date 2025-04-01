package com.maghrebia.quotegenerator.service;

import com.maghrebia.quotegenerator.dto.FilteredWeatherdata;
import com.maghrebia.quotegenerator.dto.NHTSAVinResponse;
import com.maghrebia.quotegenerator.dto.VinResult;
import com.maghrebia.quotegenerator.dto.WeatherData;
import com.maghrebia.quotegenerator.model.VehicleInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WeatherService {

    private static final String WEATHER_API_URL = "https://archive-api.open-meteo.com/v1/archive?latitude=";

    private final RestTemplate restTemplate;


    public WeatherData getWeatherInfo(Double lat, Double lng) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate oneYearAgo = currentDate.minusYears(1);
        String formattedCurrentDate = currentDate.format(formatter);
        String formattedOneYearAgoDate = oneYearAgo.format(formatter);


        String url = WEATHER_API_URL + lat + "&longitude=" + lng
                + "&start_date=" + formattedOneYearAgoDate
                + "&end_date=" + formattedCurrentDate
                + "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,weathercode,windspeed_10m_max&timezone=auto";

        ResponseEntity<WeatherData> response = restTemplate.getForEntity(url, WeatherData.class);

        return response.getBody();
    }

    public FilteredWeatherdata getAverages(WeatherData.DailyData dailyData) {
        return FilteredWeatherdata.builder()
                .averageMinTmp(getAverageMinTemperature(Objects.requireNonNull(dailyData)))
                .averageMaxTmp(getAverageMaxTemperature(Objects.requireNonNull(dailyData)))
                .averagePrecipitation(getAveragePrecipitation(Objects.requireNonNull(dailyData)))
                .averageWind(getAverageWind(Objects.requireNonNull(dailyData)))
                .build();
    }

    private double getAverageMaxTemperature(WeatherData.DailyData daily) {
        if (Objects.isNull(daily))
            return 0.0;
        return daily.temperature_2m_max().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    private double getAverageMinTemperature(WeatherData.DailyData daily) {
        if (Objects.isNull(daily))
            return 0.0;
        return daily.temperature_2m_min().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    private double getAveragePrecipitation(WeatherData.DailyData daily) {
        if (Objects.isNull(daily))
            return 0.0;
        return daily.precipitation_sum().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    private double getAverageWind(WeatherData.DailyData daily) {
        if (Objects.isNull(daily))
            return 0.0;
        return daily.windspeed_10m_max().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }
}

