package com.maghrebia.quotegenerator.service;

import com.maghrebia.quotegenerator.dto.Gender;
import com.maghrebia.quotegenerator.dto.NHTSAVinResponse;
import com.maghrebia.quotegenerator.dto.WHOApiResponse;
import com.maghrebia.quotegenerator.model.VehicleInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class WHOService {

    private static final int MIN_DATA_YEAR = 2000;
    private static final List<String> TUNISIA_INSURANCE_INDICATORS_BY_GENDER = new ArrayList<>(Arrays.asList(
            // ===== CHRONIC DISEASE CORE =====
            "BP_03",        // Crude hypertension
            "NCD_BMI_30C",  // Crude obesity
            // ===== LIFESTYLE RISKS =====
            "M_Est_tob_curr",    // Total tobacco use
            "M_Est_cig_curr",    // Cigarette-specific risk
            "SA_0000001403"
    ));

    private static final List<String> TUNISIA_INSURANCE_INDICATORS_BY_NATION = new ArrayList<>(Arrays.asList(
            // ===== HEALTHCARE ACCESS =====
            "HWF_0001",     // Doctor density
            "HWF_0006",     // Nursing staff
            // ===== MENTAL HEALTH =====
            "MH_25"         // Policy existence (regulation compliance)
    ));

    private static final String WHO_API_URL = "https://ghoapi.azureedge.net/api/";

    private final RestTemplate restTemplate;

    public List<WHOApiResponse> getWHOStats(Gender gender) {

        int year = LocalDate.now().getYear() - 1;
        List<WHOApiResponse> responses = new ArrayList<>();

        TUNISIA_INSURANCE_INDICATORS_BY_GENDER.forEach(indicator -> responses.add(callWHOApiByGender(gender, indicator, year)));

        TUNISIA_INSURANCE_INDICATORS_BY_NATION.forEach(indicator -> responses.add(callWHOApiByNation(indicator, year)));

        return responses;
    }


    private WHOApiResponse callWHOApiByGender(Gender gender, String indicator, int year) {

        if (year <= MIN_DATA_YEAR)
            return null;
        String url = WHO_API_URL + indicator +
                "?$filter=SpatialDim eq 'TUN' and TimeDim eq " + year +
                " and Dim1 eq '" + gender.getGenderCode() + "'";

        ResponseEntity<WHOApiResponse> response = restTemplate.getForEntity(url, WHOApiResponse.class);

        if (response.getBody() != null && response.getBody().value().isEmpty())
            return callWHOApiByGender(gender, indicator, --year);

        return response.getBody();
    }

    private WHOApiResponse callWHOApiByNation(String indicator, int year) {

        if (year <= MIN_DATA_YEAR)
            return null;
        String url = WHO_API_URL + indicator +
                "?$filter=SpatialDim eq 'TUN' and TimeDim eq " + year;

        ResponseEntity<WHOApiResponse> response = restTemplate.getForEntity(url, WHOApiResponse.class);

        if (response.getBody() != null && response.getBody().value().isEmpty())
            return callWHOApiByNation(indicator, --year);

        return response.getBody();
    }

}

