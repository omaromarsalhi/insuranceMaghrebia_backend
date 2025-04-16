package com.maghrebia.useraction.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_API_URL = "http://127.0.0.1:5000/recommendations";
    private final ObjectMapper objectMapper;


    public ReportResponse getAiRecommendations(List<Action> actions) {

        String url = FLASK_API_URL ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Action>> requestEntity = new HttpEntity<>(actions, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        try {
            return objectMapper.readValue(response.getBody(), ReportResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la conversion de la réponse de l'IA : " + e.getMessage(), e);
        }
    }
}
