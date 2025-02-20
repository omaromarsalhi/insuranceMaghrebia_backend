package com.maghrebia.claim.controller;

import com.maghrebia.claim.data_transfer.DTO.CreateIncidentTypeDTO;
import com.maghrebia.claim.data_transfer.mapper.IncidentTypeMapper;
import com.maghrebia.claim.entity.IncidentType;
import com.maghrebia.claim.service.IncidentTypeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("incident_type")
@RequiredArgsConstructor
public class IncidentTypeController {
    final IncidentTypeService incidentTypeService;
    final IncidentTypeMapper incidentTypeMapper;
    @PostMapping
    public void saveIncidentType(@RequestBody CreateIncidentTypeDTO createIncidentTypeDTO) {
        IncidentType incidentType = incidentTypeMapper.toEntity(createIncidentTypeDTO);
        System.out.println(incidentType.isActive());
        incidentType.setCreated(LocalDateTime.now());
        incidentTypeService.saveIncidentType(incidentType);
    }

    @GetMapping
    public List<IncidentType> findAllIncidentTypes() {
        return incidentTypeService.getAllIncidentTypes();
    }

    @DeleteMapping("/{id}")
    public void deleteIncidentType(@PathVariable("id") String id) {
        incidentTypeService.deleteIncidentType(id);
    }
}
