package com.maghrebia.quotegenerator.model;

import lombok.Data;


@Data
public class VehicleInfo {

    // Vehicle Identification (Core risk factors)
    private String vin;
    private String make;
    private String model;
    private String modelYear;
    private String vehicleType;

    // Engine/Fuel Type (Risk modifiers)
    private String fuelTypePrimary;
    private String displacementL;

    // Safety Features (Premium discounts)
    private String abs;
    private String tractionControl;
    private String electronicStabilityControl;
    private String backupCamera;
    private String daytimeRunningLights;

    // Vehicle Characteristics (Risk profile)
    private String bodyClass;
    private String doors;
    private String seatingCapacity;
    private String grossVehicleWeightRating;

    // Manufacturer/Plant Info (Fraud indicators)
    private String plantCountry;
}
