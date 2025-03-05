package com.maghrebia.claim.service;

import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimService {
    final ClaimRepository claimRepository;
    final ImageService imageService;
    public void save(Claim claim) {
        claimRepository.save(claim);
    }

    public List<Claim> findAll(boolean withImages) {
        List<Claim> claims = claimRepository.findAll();
        if (withImages)
            for(Claim claim : claims) {
                claim.setImages(claim.getImages().stream().map(ImageService::convertImageToBase64).collect(Collectors.toList()));
            }
        return claims;
    }

    public Claim findById(String id, boolean withImages) {
        Claim claim = claimRepository.findById(id).orElse(null);
        if(claim != null && withImages)
            claim.setImages(claim.getImages().stream().map(ImageService::convertImageToBase64).collect(Collectors.toList()));
        return claim;
    }
}
