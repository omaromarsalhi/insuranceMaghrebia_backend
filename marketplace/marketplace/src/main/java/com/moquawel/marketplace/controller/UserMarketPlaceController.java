package com.moquawel.marketplace.controller;


import com.moquawel.marketplace.mkservice.MyService;
import com.moquawel.marketplace.service.MyServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace/users")
@RequiredArgsConstructor
public class UserMarketPlaceController {

    private final MyServiceService myServiceService;

    @GetMapping("/service/get-by-category/{categoryId}")
    public ResponseEntity<List<MyService>> getServicesByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(myServiceService.getServicesByCategory(categoryId));
    }

}
