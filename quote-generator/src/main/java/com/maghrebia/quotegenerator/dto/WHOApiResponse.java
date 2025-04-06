package com.maghrebia.quotegenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;


public record WHOApiResponse(
        @JsonProperty("value")
        List<WHOData> value
) {
    public record WHOData(
            @JsonProperty("IndicatorCode")
            String indicatorCode,
            @JsonProperty("SpatialDimType")
            String spatialDimType,
            @JsonProperty("SpatialDim")
            String spatialDim,
            @JsonProperty("TimeDimType")
            String timeDimType,
            @JsonProperty("Dim1Type")
            String dim1Type,
            @JsonProperty("TimeDim")
            Integer timeDim,
            @JsonProperty("Dim1")
            String dim1,
            @JsonProperty("NumericValue")
            Float numericValue,
            @JsonProperty("Low")
            Float low,
            @JsonProperty("High")
            Float high,
            @JsonProperty("TimeDimensionValue")
            String timeDimensionValue
    ) {
    }
}
