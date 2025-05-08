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
@Table(name = "health")
public class Health {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer healthId;

    private int age;

    private String gender;

    private String governorate;

    private String occupation;

    @ElementCollection
    private List<String> preExistingConditions;

    @ElementCollection
    private List<String> familyHistory;

    private String hospitalizations;

    private String chronicIllnesses;

    private String surgeries;

    private String smoking;

    private String alcohol;

    private String exercise;

    private Float  bmi;

    private String planType;

    private int deductible;

    @ElementCollection
    private List<String> addOns;

    private String travelFrequency;

    @ElementCollection
    private List<String> vaccinations;

}
