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
    public List<IncidentType> getIncidentTypes(boolean status) {
        return incidentTypeRepository.findAllByActive(status);
    }

    public void deleteIncidentType(String id) {
        incidentTypeRepository.deleteById(id);
    }

    public void patchIncidentTypeStatus(String id, boolean status) {
        IncidentType incidentType = incidentTypeRepository.findById(id).get();
        incidentType.setActive(status);
        incidentTypeRepository.save(incidentType);
    }
}
