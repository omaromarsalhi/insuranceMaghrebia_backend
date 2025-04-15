package com.maghrebia.hr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maghrebia.hr.dto.request.ModelRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FastApiClient {

    private final String fastApiUrl = "http://localhost:8040/analyze-resume";
    private final RestTemplate restTemplate = new RestTemplate();

    public String sendPdfToGemini(MultipartFile file, ModelRequest modelRequest) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Convert modelRequest to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonModel = mapper.writeValueAsString(modelRequest);
        body.add("modelRequest", jsonModel);

        // Wrap PDF
        ByteArrayResource pdf = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", pdf);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("FastAPI error: " + response.getBody());
            throw new RuntimeException("FastAPI failed: " + response.getStatusCode());
        }
        String responseString = (String) response.getBody().get("result");
        return responseString.replace("```json", "").replace("```", "").trim();
    }
}