package com.maghrebia.appointement.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.maghrebia.appointement.model.OfferType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder


public record AppointmentDto(
        Integer appointmentId,
        String firstName,
        String lastName,
        String email,
        Long phone,
        LocalDateTime dob,
        Long cin,
        OfferType offerType,
        GeneratedQuoteDto generatedQuote,
        AutomobileDto offerDetails
) {
}
