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

    public List<Claim> findAll(boolean withImages, boolean withResponses) {
        List<Claim> claims = claimRepository.findAll();

        for(Claim claim : claims) {
            claim = prepareClaim(claim, withImages, withResponses);
        }
        return claims;
    }

    public Claim findById(String id, boolean withImages, boolean withResponses) {
        Claim claim = claimRepository.findById(id).orElse(null);
        claim = prepareClaim(claim, withImages, withResponses);
        return claim;
    }

    public List<Claim> findAllByUserId(String userId, boolean withImages, boolean withResponses) {
        List<Claim> claims = claimRepository.findAllByUserId(userId);
        for(Claim claim : claims) {
            claim = prepareClaim(claim, withImages, withResponses);
        }
        return claims;
    }

    private Claim prepareClaim(Claim claim, boolean withImages, boolean withResponses) {
        if(claim != null && withImages)
            claim.setImages(loadImages(claim.getImages()));
        if(!withResponses)
            claim.getResponses().clear();
        return claim;
    }
    private List<String> loadImages(List<String> imagesPaths) {
        return imagesPaths.stream().map(ImageService::convertImageToBase64).collect(Collectors.toList());
    }
}
