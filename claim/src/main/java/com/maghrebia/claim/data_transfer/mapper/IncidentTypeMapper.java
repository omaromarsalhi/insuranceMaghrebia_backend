package com.maghrebia.claim.data_transfer.mapper;

import com.maghrebia.claim.data_transfer.DTO.CreateIncidentTypeDTO;
import com.maghrebia.claim.entity.IncidentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IncidentTypeMapper {

    @Named("mapIncidentType")
    default IncidentType mapIncidentType(String objectBId) {
        if (objectBId == null) {
            return null;
        }
        IncidentType type = new IncidentType();
         type.setId(objectBId);
        return type;
    }

    IncidentType toEntity(CreateIncidentTypeDTO createIncidentTypeDTO);
}
