package com.maghrebia.claim.data_transfer.mapper;

import com.maghrebia.claim.data_transfer.DTO.CreateResponseDTO;
import com.maghrebia.claim.entity.Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ResponseMapper {

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    Response toEntity(CreateResponseDTO dto);
}
