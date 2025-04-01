package com.maghrebia.quotegenerator.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public record AutoInsuranceRequest(
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
        AddressInfo addressInfo,
        BillingPeriod billingPeriod
) {
}
