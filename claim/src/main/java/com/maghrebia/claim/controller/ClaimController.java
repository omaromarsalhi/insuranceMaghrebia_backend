package com.maghrebia.claim.controller;

import com.maghrebia.claim.data_transfer.DTO.CreateClaimDTO;
import com.maghrebia.claim.data_transfer.mapper.ClaimMapper;
import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.entity.ClaimStatus;
import com.maghrebia.claim.service.ClaimService;
import com.maghrebia.claim.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("claim")
@RequiredArgsConstructor
public class ClaimController {
    final ClaimService claimService;
    final ClaimMapper claimMapper;
    final ImageService imageService;

    @PostMapping
    public ResponseEntity<Void> addClaim(@RequestBody CreateClaimDTO dto) {
        Claim claim = claimMapper.toEntity(dto);
        claim.setSubmitDate(LocalDateTime.now());
        ArrayList<String> paths = new ArrayList<>();
        for(String image: dto.getImages() ) {
            paths.add(imageService.saveImage(image));
        }
        claim.setImages(paths);
        claim.setStatus(ClaimStatus.NEW);
        claimService.save(claim);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Claim>> getClaims(
            @RequestParam(required = false, defaultValue = "false") boolean includeImages,
            @RequestParam(required = false, defaultValue = "false") boolean includeResponses,
            @RequestParam(required = false) String userId
            ) {
        if(userId != null) {
            return ResponseEntity.accepted().body(claimService.findAllByUserId(userId, includeImages, includeResponses));
        }
        return ResponseEntity.accepted().body(claimService.findAll(includeImages, includeResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaim(
            @RequestParam(required = false, defaultValue = "false") boolean includeImages,
            @RequestParam(required = false, defaultValue = "false") boolean includeResponses,
            @PathVariable String id
    ) {
        return ResponseEntity.accepted().body(claimService.findById(id, includeImages, includeResponses));
    }
}
