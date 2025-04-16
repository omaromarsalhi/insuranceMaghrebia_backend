package com.maghrebia.claim.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FastApiClient {

    private final String fastApiUrl = "http://localhost:8000/process-image/";
    private final RestTemplate restTemplate = new RestTemplate();

    public String sendImageToGemini(String imageBase64) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Simple JSON with just the image
            String requestBody = String.format("{\"image\": \"%s\"}", imageBase64);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fastApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return response.getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}