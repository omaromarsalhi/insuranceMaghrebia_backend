package com.maghrebia.appointement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "automobile")
public class Automobile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String autoId;

    private String licenseNumber;

    private Integer drivingExperience;

    private String vehicleType;

    private String vehicleMake;

    private String vehicleModel;

    private String accidentHistory;

    private Boolean trafficViolations;

    private Boolean defensiveDrivingCourse;

    private String coverageType;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;

}
