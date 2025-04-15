package com.maghrebia.appointement.mapper;

import com.maghrebia.appointement.dto.AppointmentDto;
import com.maghrebia.appointement.dto.AutomobileDto;
import com.maghrebia.appointement.dto.GeneratedQuoteDto;
import com.maghrebia.appointement.dto.LocationDto;
import com.maghrebia.appointement.model.Appointment;
import com.maghrebia.appointement.model.Automobile;
import com.maghrebia.appointement.model.GeneratedQuote;
import com.maghrebia.appointement.model.Location;

public class AppointmentMapper {


    public static AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .firstName(appointment.getFirstName())
                .lastName(appointment.getLastName())
                .email(appointment.getEmail())
                .phone(appointment.getPhone())
                .dob(appointment.getDob())
                .cin(appointment.getCin())
                .build();
    }

    public static AppointmentDto toDto(Appointment appointment, AutomobileDto automobile,GeneratedQuoteDto generatedQuote) {
        return AppointmentDto.builder()
                .firstName(appointment.getFirstName())
                .lastName(appointment.getLastName())
                .email(appointment.getEmail())
                .phone(appointment.getPhone())
                .dob(appointment.getDob())
                .cin(appointment.getCin())
                .offerType(appointment.getOfferType())
                .offerDetails(automobile)
                .generatedQuote(generatedQuote)
                .build();
    }

    public static Appointment toEntity(AppointmentDto dto) {
        Appointment appointment = new Appointment();
        appointment.setFirstName(dto.firstName());
        appointment.setLastName(dto.lastName());
        appointment.setEmail(dto.email());
        appointment.setPhone(dto.phone());
        appointment.setDob(dto.dob());
        appointment.setCin(dto.cin());
        appointment.setOfferType(dto.offerType());
        appointment.setAutomobile(toOfferDetailsEntity(dto.offerDetails()));
        appointment.setGeneratedQuote(toGeneratedQuoteEntity(dto.generatedQuote()));
        return appointment;
    }


    public static Automobile toOfferDetailsEntity(AutomobileDto dto) {
        System.out.println(dto);
        Automobile automobile = new Automobile();
        automobile.setAccidentHistory(dto.accidentHistory());
        automobile.setCoverageType(dto.coverageType());
        automobile.setDrivingExperience(dto.drivingExperience());
        automobile.setLicenseNumber(dto.licenseNumber());
        automobile.setTrafficViolations(dto.trafficViolations());
        automobile.setVehicleModel(dto.vehicleModel());
        automobile.setVehicleType(dto.vehicleType());
        automobile.setDefensiveDrivingCourse(dto.defensiveDrivingCourse());
        automobile.setLocation(toLocationEntity(dto.addressInfo()));
        return automobile;
    }


    public static AutomobileDto toOfferDetailsDto(Automobile entity) {
        return AutomobileDto.builder()
                .accidentHistory(entity.getAccidentHistory())
                .coverageType(entity.getCoverageType())
                .drivingExperience(entity.getDrivingExperience())
                .licenseNumber(entity.getLicenseNumber())
                .trafficViolations(entity.getTrafficViolations())
                .vehicleModel(entity.getVehicleModel())
                .vehicleType(entity.getVehicleType())
                .defensiveDrivingCourse(entity.getDefensiveDrivingCourse())
                .addressInfo(toLocationDto(entity.getLocation()))
                .build();
    }

    public static Location toLocationEntity(LocationDto dto) {
        if (dto == null) return null;
        Location location = new Location();
        location.setStreetNumber(dto.streetNumber());
        location.setStreetName(dto.streetName());
        location.setMunicipality(dto.municipality());
        location.setCountrySubdivision(dto.countrySubdivision());
        location.setCountrySubdivisionName(dto.countrySubdivisionName());
        location.setCountrySubdivisionCode(dto.countrySubdivisionCode());
        location.setPostalCode(dto.postalCode());
        location.setExtendedPostalCode(dto.extendedPostalCode());
        location.setCountryCode(dto.countryCode());
        location.setCountry(dto.country());
        location.setCountryCodeISO3(dto.countryCodeISO3());
        location.setFreeformAddress(dto.freeformAddress());
        location.setLocalName(dto.localName());
        location.setNorthEastLat(dto.boundingBox().northEast().lat());
        location.setNorthEastLng(dto.boundingBox().northEast().lng());
        location.setSouthWestLat(dto.boundingBox().southWest().lat());
        location.setSouthWestLng(dto.boundingBox().southWest().lng());
        return location;
    }


    public static LocationDto toLocationDto(Location entity) {
        return LocationDto.builder()
                .streetNumber(entity.getStreetNumber())
                .streetName(entity.getStreetName())
                .municipality(entity.getMunicipality())
                .countrySubdivision(entity.getCountrySubdivision())
                .countrySubdivisionName(entity.getCountrySubdivisionName())
                .countrySubdivisionCode(entity.getCountrySubdivisionCode())
                .postalCode(entity.getPostalCode())
                .extendedPostalCode(entity.getExtendedPostalCode())
                .countryCode(entity.getCountryCode())
                .country(entity.getCountry())
                .countryCodeISO3(entity.getCountryCodeISO3())
                .freeformAddress(entity.getFreeformAddress())
                .localName(entity.getLocalName())
                .build();
    }


    public static GeneratedQuote toGeneratedQuoteEntity(GeneratedQuoteDto dto) {
        GeneratedQuote generatedQuote = new GeneratedQuote();
        generatedQuote.setAmount(dto.amount());
        generatedQuote.setCurrency(dto.currency());
        generatedQuote.setBillingPeriod(dto.billingPeriod());
        generatedQuote.setValidFrom(dto.validFrom());
        generatedQuote.setValidTo(dto.validTo());
        generatedQuote.setBaseAnnualPremium(dto.baseAnnualPremium());
        return generatedQuote;
    }

    public static GeneratedQuoteDto toGeneratedQuoteDto(GeneratedQuote entity) {
        return GeneratedQuoteDto.builder()
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .billingPeriod(entity.getBillingPeriod())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .baseAnnualPremium(entity.getBaseAnnualPremium())
                .validTo(entity.getValidTo())
                .build();
    }
}
