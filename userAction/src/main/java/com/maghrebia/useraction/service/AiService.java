package com.maghrebia.useraction.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maghrebia.useraction.entity.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@RequiredArgsConstructor
public class AiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_API_URL = "http://127.0.0.1:5000/recommendations/";
    private final ObjectMapper objectMapper;


    public ReportResponse getAiRecommendations(String userId) {
        String url = FLASK_API_URL + userId;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        try {
            return objectMapper.readValue(response.getBody(), ReportResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la conversion de la réponse de l'IA : " + e.getMessage(), e);
        }
    }
}
