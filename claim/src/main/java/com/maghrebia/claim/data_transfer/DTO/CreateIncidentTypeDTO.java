package com.maghrebia.claim.data_transfer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateIncidentTypeDTO {
    String name;
    String description;
    boolean active;
}
