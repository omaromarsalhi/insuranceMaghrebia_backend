package com.maghrebia.quotegenerator.dto;

import java.util.List;

public record WeatherData(
        double latitude,
        double longitude,
        double generationtime_ms,
        int utc_offset_seconds,
        String timezone,
        String timezone_abbreviation,
        double elevation,
        DailyUnits daily_units,
        DailyData daily
) {
    public record DailyUnits(
            String time,
            String temperature_2m_max,
            String temperature_2m_min,
            String precipitation_sum,
            String weathercode,
            String windspeed_10m_max
    ) {}

    public record DailyData(
            List<String> time,
            List<Double> temperature_2m_max,
            List<Double> temperature_2m_min,
            List<Double> precipitation_sum,
            List<Integer> weathercode,
            List<Double> windspeed_10m_max
    ) {}
}