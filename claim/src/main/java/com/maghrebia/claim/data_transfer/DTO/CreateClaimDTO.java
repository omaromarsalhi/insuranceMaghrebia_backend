package com.maghrebia.claim.data_transfer.DTO;

import com.maghrebia.claim.entity.IncidentType;
import com.maghrebia.claim.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CreateClaimDTO {
    String incidentTypeId;
    String title;
    String userId;
    String incidentLocation;
    String locationCoordinates;
    LocalDateTime incidentDate;
    String description;
    String[] images;
}
