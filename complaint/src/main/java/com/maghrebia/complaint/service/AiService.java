package com.maghrebia.complaint.service;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "http://127.0.0.1:5000/check-compatibility";
    private final String apiUrlTitle = "http://127.0.0.1:5000/getTitle";

    public String getComplaintResponse(String title, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = String.format("{\"title\": \"%s\", \"description\": \"%s\"}", title, description);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("response");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API Flask : " + e.getMessage());
        }

        return "Erreur de communication avec l'API Flask";
    }

    public boolean isComplaintValid(String title, String description) {
        String result = getComplaintResponse(title, description);
        System.err.println("Résultat de la vérification : " + result);
        return "compatible".equalsIgnoreCase(result);
    }

    public String getSuggestedTitle(String description) {
        System.err.println(description);
        if (description == null || description.isEmpty()) {
            return "No description provided.";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = String.format("{\"description\": \"%s\"}", description);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrlTitle, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String result = (String) response.getBody().get("response");
                return result;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API Flask: " + e.getMessage());
        }
        return "Error occurred while calling the Flask API.";
    }
}
