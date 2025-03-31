package com.maghrebia.quotegenerator.controller;

import com.maghrebia.quotegenerator.model.VehicleInfo;
import com.maghrebia.quotegenerator.service.VINDecoderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/VINDecoder")
public class VINDecoderController {

    private final VINDecoderService vinDecoderService;


    @GetMapping("/decode/{vin}")
    public VehicleInfo getVehicleInfo(@PathVariable String vin) {
        return vinDecoderService.getVehicleInfo(vin);
    }
}
