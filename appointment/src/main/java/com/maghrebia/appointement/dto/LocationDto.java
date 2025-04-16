package com.maghrebia.appointement.dto;


import lombok.Builder;

@Builder
public record LocationDto(
        String streetNumber,
        String streetName,
        String municipality,
        String countrySubdivision,
        String countrySubdivisionName,
        String countrySubdivisionCode,
        String postalCode,
        String extendedPostalCode,
        String countryCode,
        String country,
        String countryCodeISO3,
        String freeformAddress,
        String localName,
        BoundingBox boundingBox
) {
    public record BoundingBox(
            Position northEast,
            Position southWest,
            String entity
    ) {}

    public record Position(
            double lng,
            double lat
    ) {}
}