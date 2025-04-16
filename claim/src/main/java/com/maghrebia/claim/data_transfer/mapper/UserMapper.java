package com.maghrebia.claim.data_transfer.mapper;

import com.maghrebia.claim.entity.IncidentType;
import com.maghrebia.claim.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("mapUser")
    default User mapUser(String objectBId) {
        if (objectBId == null) {
            return null;
        }
        User type = new User();
        type.setId(objectBId);
        return type;
    }
}
