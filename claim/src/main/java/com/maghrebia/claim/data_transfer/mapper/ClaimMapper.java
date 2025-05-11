package com.maghrebia.claim.data_transfer.mapper;


import com.maghrebia.claim.data_transfer.DTO.CreateClaimDTO;
import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.entity.IncidentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {IncidentTypeMapper.class,  UserMapper.class})
public interface ClaimMapper {

    @Named("mapClaim")
    default Claim mapClaim(String objectBId) {
        if (objectBId == null) {
            return null;
        }
        Claim type = new Claim();
        type.setId(objectBId);
        return type;
    }

    @Mapping(source = "incidentTypeId", target = "incidentType", qualifiedByName = "mapIncidentType")
    @Mapping(target = "images", ignore = true)
    Claim toEntity(CreateClaimDTO dto);
}
