package com.maghrebia.offer.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "retrieveClient", url = "${search-service.url}")
public interface RetrieveClient {
    @PostMapping
    ResponseEntity<?> retrieveClient(@RequestBody Map<String, Object> payload);

}


