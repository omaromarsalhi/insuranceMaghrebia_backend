package com.maghrebia.quotegenerator.controller;

import com.maghrebia.quotegenerator.dto.AutoInsuranceRequest;
import com.maghrebia.quotegenerator.dto.QuoteResponse;
import com.maghrebia.quotegenerator.model.VehicleInfo;
import com.maghrebia.quotegenerator.service.AutomobileQuoteService;
import com.maghrebia.quotegenerator.service.VINDecoderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/automobile")
public class AutomobileQuoteController {

    private final AutomobileQuoteService automobileQuoteService;

    @PostMapping("/calculate")
    public ResponseEntity<QuoteResponse> calculate(@RequestBody AutoInsuranceRequest autoInsuranceRequest) {
        return ResponseEntity.ok(automobileQuoteService.calculate(autoInsuranceRequest));
    }
}
