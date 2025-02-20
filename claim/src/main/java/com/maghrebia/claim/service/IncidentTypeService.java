package com.maghrebia.claim.service;

import com.maghrebia.claim.entity.IncidentType;
import com.maghrebia.claim.repository.IncidentTypeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentTypeService {
    private final IncidentTypeRepository incidentTypeRepository;

    public void saveIncidentType(IncidentType incidentType) {
        incidentTypeRepository.save(incidentType);
    }

    public List<IncidentType> getAllIncidentTypes() {
        return incidentTypeRepository.findAll();
    }

    public void deleteIncidentType(String id) {
        incidentTypeRepository.deleteById(id);
    }
}
