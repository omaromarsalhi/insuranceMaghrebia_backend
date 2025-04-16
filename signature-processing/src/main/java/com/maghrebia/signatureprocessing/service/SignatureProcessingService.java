package com.maghrebia.signatureprocessing.service;

import com.maghrebia.signatureprocessing.entity.SignatureRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class SignatureProcessingService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String SIGNATURE_URL = "http://localhost:8000/signature/search-signature";

    public ResponseEntity<String> verifyService(SignatureRequest signatureRequest) {
        HttpEntity<SignatureRequest> entity = new HttpEntity<>(signatureRequest);

        return restTemplate.exchange(
                SIGNATURE_URL,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

}
