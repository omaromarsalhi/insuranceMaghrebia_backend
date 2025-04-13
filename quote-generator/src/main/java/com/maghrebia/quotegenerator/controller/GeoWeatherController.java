package com.maghrebia.quotegenerator.controller;

import com.maghrebia.quotegenerator.dto.AddressInfo;
import com.maghrebia.quotegenerator.dto.GeoWeatherData;
import com.maghrebia.quotegenerator.model.VehicleInfo;
import com.maghrebia.quotegenerator.service.AutomobileQuoteService;
import com.maghrebia.quotegenerator.service.VINDecoderService;
import com.maghrebia.quotegenerator.service.WHOService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/geoweather")
public class GeoWeatherController {

    private final AutomobileQuoteService automobileQuoteService;

    @PostMapping
    public GeoWeatherData getGeoWeatherData(@RequestBody AddressInfo addressInfo) {
        return automobileQuoteService.getGeoWeatherData(addressInfo);
    }

}
