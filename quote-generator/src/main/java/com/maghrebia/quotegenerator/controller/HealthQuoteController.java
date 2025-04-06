package com.maghrebia.quotegenerator.controller;

import com.maghrebia.quotegenerator.dto.AutoInsuranceRequest;
import com.maghrebia.quotegenerator.dto.HealthInsuranceRequest;
import com.maghrebia.quotegenerator.dto.QuoteResponse;
import com.maghrebia.quotegenerator.service.AutomobileQuoteService;
import com.maghrebia.quotegenerator.service.HealthQuoteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/health")
public class HealthQuoteController {


    private final HealthQuoteService healthQuoteService;

    @PostMapping("/calculate")
    public ResponseEntity<QuoteResponse> calculateHealth(@RequestBody HealthInsuranceRequest healthInsuranceRequest) {
        return ResponseEntity.ok(healthQuoteService.calculate(healthInsuranceRequest));
    }
}
