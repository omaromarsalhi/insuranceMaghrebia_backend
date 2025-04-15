package com.maghrebia.appointement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    String streetNumber;
    String streetName;
    String municipality;
    String countrySubdivision;
    String countrySubdivisionName;
    String countrySubdivisionCode;
    String postalCode;
    String extendedPostalCode;
    String countryCode;
    String country;
    String countryCodeISO3;
    String freeformAddress;
    String localName;

    double northEastLng;
    double northEastLat;
    double southWestLng;
    double southWestLat;
    String entity;
}
