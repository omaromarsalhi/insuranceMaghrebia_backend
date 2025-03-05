package com.maghrebia.claim.data_transfer.mapper;


import com.maghrebia.claim.data_transfer.DTO.CreateClaimDTO;
import com.maghrebia.claim.entity.Claim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IncidentTypeMapper.class,  UserMapper.class})
public interface ClaimMapper {

    @Mapping(source = "incidentTypeId", target = "incidentType", qualifiedByName = "mapIncidentType")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(target = "images", ignore = true)
    Claim toEntity(CreateClaimDTO dto);
}
