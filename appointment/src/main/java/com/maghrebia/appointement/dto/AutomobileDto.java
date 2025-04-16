package com.maghrebia.appointement.dto;

import lombok.Builder;

@Builder
public record AutomobileDto(
        Integer autoId,
        String vin,
        String licenseNumber,
        Integer drivingExperience,
        String vehicleType,
        String vehicleMake,
        String vehicleModel,
        String accidentHistory,
        Boolean trafficViolations,
        Boolean defensiveDrivingCourse,
        String coverageType,
        LocationDto addressInfo,
        String billingPeriod
) {
}
