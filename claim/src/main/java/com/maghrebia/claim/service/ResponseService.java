package com.maghrebia.claim.service;

import com.maghrebia.claim.entity.Response;
import com.maghrebia.claim.repository.ClaimRepository;
import com.maghrebia.claim.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {
    final ResponseRepository responseRepository;
    final ClaimRepository claimRepository;

    public void save(Response response) {
        responseRepository.save(response);
    }
    public List<Response> findAllByClaimId(String claimId) {
        return claimRepository.findById(claimId).get().getResponses();
    }
}
