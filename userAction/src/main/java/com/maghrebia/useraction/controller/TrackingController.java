package com.maghrebia.useraction.controller;

import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.UserAction;
import com.maghrebia.useraction.service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/userTrack")
@CrossOrigin(origins = {"http://localhost:58138", "http://localhost:4200"})
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addComplaint(@PathVariable String userId,@RequestBody Action action) {
        return ResponseEntity.ok(trackingService.saveUserAction(userId,action));
    }

}
