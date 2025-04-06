package com.maghrebia.quotegenerator.dto;


import lombok.Builder;

@Builder
public record FilteredWeatherdata(
        Double averageMinTmp,
        Double averageMaxTmp,
        Double averagePrecipitation,
        Double averageWind
) {
}
