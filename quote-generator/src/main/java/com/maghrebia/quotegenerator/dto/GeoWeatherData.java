package com.maghrebia.quotegenerator.dto;


import lombok.Builder;

@Builder
public record GeoWeatherData(
        Double averageMinTmp,
        Double averageMaxTmp,
        Double averagePrecipitation,
        Double averageWind,
        boolean isUrbanArea
) {
}
