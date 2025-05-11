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
@RequestMapping("/api/v1/who")
public class WHOController {

    private final WHOService whoService;

    @GetMapping("/{gender}")
    public List<WHOApiResponse> getVehicleInfo(@PathVariable Gender gender) {
        return whoService.getWHOStats(gender);
    }

}
