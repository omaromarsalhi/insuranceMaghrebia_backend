package com.maghrebia.useraction.controller;
import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/userTrack")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addComplaint(@PathVariable String userId, @RequestBody Action action) {
        return ResponseEntity.ok(trackingService.saveUserAction(userId, action));
    }

    @GetMapping("/{userId}")
    public Map<LocalDate, Integer> getUserScoresPerDay(@PathVariable String userId) {
        return trackingService.getUserScoresPerDay(userId);
    }


}
