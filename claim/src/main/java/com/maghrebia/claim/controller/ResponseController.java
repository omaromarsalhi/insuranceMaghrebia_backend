package com.maghrebia.claim.controller;

import com.maghrebia.claim.data_transfer.DTO.CreateResponseDTO;
import com.maghrebia.claim.data_transfer.mapper.ResponseMapper;
import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.entity.Response;
import com.maghrebia.claim.service.ClaimService;
import com.maghrebia.claim.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("response")
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;
    private final ResponseMapper responseMapper;
    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<Void> saveResponse(@RequestBody CreateResponseDTO dto) {
        Response response = responseMapper.toEntity(dto);
        Claim claim = claimService.findById(dto.getClaimId(), false, true);
        claim.setStatus(dto.getClaimStatus());
        response.setRespondedAt(LocalDateTime.now());
        claim.getResponses().add(response);
        responseService.save(response);
        claimService.save(claim);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
