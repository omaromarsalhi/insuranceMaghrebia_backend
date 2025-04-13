package com.maghrebia.appointement.controller;

import com.maghrebia.appointement.dto.AppointmentDto;
import com.maghrebia.appointement.dto.AutomobileDto;
import com.maghrebia.appointement.model.Appointment;
import com.maghrebia.appointement.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {


    private final AppointmentService appointmentService;


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AppointmentDto appointment) {
        appointmentService.save(appointment);
        return ResponseEntity.ok().build();
    }

}
