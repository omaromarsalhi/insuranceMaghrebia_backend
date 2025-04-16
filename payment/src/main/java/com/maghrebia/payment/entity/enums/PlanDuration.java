package com.maghrebia.payment.entity.enums;

import lombok.Getter;

@Getter
public enum PlanDuration {
    ONE_MONTH(1, "1 month"),
    THREE_MONTHS(3, "3 months"),
    SIX_MONTHS(6, "6 months"),
    ONE_YEAR(12, "1 year");

    private final int months;
    private final String label;

    PlanDuration(int months, String label) {
        this.months = months;
        this.label = label;
    }

    public static PlanDuration fromLabel(String label) {
        for (PlanDuration duration : values()) {
            if (duration.label.equalsIgnoreCase(label)) {
                return duration;
            }
        }
        throw new IllegalArgumentException("Invalid plan duration: " + label);
    }
}
