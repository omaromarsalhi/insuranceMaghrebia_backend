package com.maghrebia.appointement.mapper;

import com.maghrebia.appointement.dto.HealthDto;

import java.util.List;
import java.util.Map;

public class ObjectMapper {
    public static HealthDto convertObjectToHealthDto(Object source) {
        if (source instanceof Map<?, ?> map) {
            return HealthDto.builder()
                    .age(getInteger(map, "age"))
                    .gender(getString(map, "gender"))
                    .governorate(getString(map, "governorate"))
                    .occupation(getString(map, "occupation"))
                    .preExistingConditions(getStringList(map, "preExistingConditions"))
                    .familyHistory(getStringList(map, "familyHistory"))
                    .hospitalizations(getString(map, "hospitalizations"))
                    .chronicIllnesses(getString(map, "chronicIllnesses"))
                    .surgeries(getString(map, "surgeries"))
                    .smoking(getString(map, "smoking"))
                    .alcohol(getString(map, "alcohol"))
                    .exercise(getString(map, "exercise"))
                    .bmi(getFloat(map, "bmi"))
                    .planType(getString(map, "planType"))
                    .deductible(getInteger(map, "deductible"))
                    .addOns(getStringList(map, "addOns"))
                    .travelFrequency(getString(map, "travelFrequency"))
                    .vaccinations(getStringList(map, "vaccinations"))
                    .build();
        }
        throw new IllegalArgumentException("Unsupported source type: " + source.getClass().getName());
    }

    // Helper methods for safe type conversion
    private static Integer getInteger(Map<?, ?> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private static Float getFloat(Map<?, ?> map, String key) {
        Object value = map.get(key);
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value != null) {
            try {
                return Float.parseFloat(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private static String getString(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private static List<String> getStringList(Map<?, ?> map, String key) {
        Object value = map.get(key);
        if (value instanceof List<?>) {
            try {
                return (List<String>) value;
            } catch (ClassCastException e) {
                // Handle case where list contains non-String objects
                List<?> rawList = (List<?>) value;
                return rawList.stream()
                        .map(Object::toString)
                        .toList();
            }
        }
        return List.of(); // Return empty list if null or not a list
    }
}
