package com.maghrebia.claim.data_transfer.mapper;

import com.maghrebia.claim.data_transfer.DTO.CreateIncidentTypeDTO;
import com.maghrebia.claim.entity.IncidentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidentTypeMapper {

    IncidentType toEntity(CreateIncidentTypeDTO createIncidentTypeDTO);
}
