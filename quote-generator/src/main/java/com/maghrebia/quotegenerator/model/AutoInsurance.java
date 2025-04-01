package com.maghrebia.quotegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "offer")
public class AutoInsurance {

    @Id
    private Long id;

    private String licenseNumber;

    private Integer drivingExperience;

    private String vehicleType;

    private String vehicleMake;

    private String vehicleModel;

    private String accidentHistory;

    private List<String> trafficViolations;

    private Boolean defensiveDrivingCourse;

    private String coverageType;

    private String location;
}
