package com.maghrebia.quotegenerator.controller;

import com.maghrebia.quotegenerator.dto.Gender;
import com.maghrebia.quotegenerator.dto.WHOApiResponse;
import com.maghrebia.quotegenerator.model.VehicleInfo;
import com.maghrebia.quotegenerator.service.VINDecoderService;
import com.maghrebia.quotegenerator.service.WHOService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final VINDecoderService vinDecoderService;
    private final WHOService whoService;


    @GetMapping("/decode/{vin}")
    public VehicleInfo getVehicleInfo(@PathVariable String vin) {
        return vinDecoderService.getVehicleInfo(vin);
    }

    @GetMapping("/who")
    public List<WHOApiResponse> getWhoStats() {
        return whoService.getWHOStats(Gender.MALE);
    }
}
