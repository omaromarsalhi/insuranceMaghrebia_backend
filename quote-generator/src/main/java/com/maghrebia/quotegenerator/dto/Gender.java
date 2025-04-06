package com.maghrebia.quotegenerator.dto;

public enum Gender {
    MALE("SEX_MLE"),
    FEMALE("SEX_FMLE");

    private final String genderCode;

    // Constructor
    Gender(String genderCode) {
        this.genderCode = genderCode;
    }

    // Getter method for genderCode
    public String getGenderCode() {
        return genderCode;
    }
}
