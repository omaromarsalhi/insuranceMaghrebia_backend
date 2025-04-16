package com.maghrebia.claim.data_transfer.DTO;

import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.entity.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateResponseDTO {
    String claimId;
    String userId;
    String response;
    ClaimStatus claimStatus;
}
