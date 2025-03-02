package com.maghrebia.blockchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maghrebia.blockchain.dto.Blockchain;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor

public class BlockchainService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BLOCKCHAIN_API_URL = "http://localhost:3000/blockchain";

    public Blockchain getPayment(int index) {
        String url = BLOCKCHAIN_API_URL + "/get-payment/" + index;

        try {

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Blockchain blockchain = objectMapper.readValue(response.getBody(), Blockchain.class);
            return blockchain;

        } catch (Exception e) {
            System.out.println("Error parsing blockchain response: {}"+ e.getMessage());
            throw new RuntimeException("Error parsing blockchain response: " + e.getMessage());
        }
    }
    public String addPayment(Blockchain blockchain) {
        String url = BLOCKCHAIN_API_URL + "/add-blockchain/";

        Blockchain blockchainToAdd = new Blockchain();
        blockchainToAdd.setPaymentId(blockchain.getPaymentId());
        blockchainToAdd.setAmount(blockchain.getAmount());
        blockchainToAdd.setFullname(blockchain.getFullname());

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(blockchainToAdd),
                    String.class);

            if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                String errorMessage = "Blockchain service returned 500 error: " + response.getBody();
                throw new RuntimeException(errorMessage);
            }
            JSONObject responseObject = new JSONObject(response.getBody());

            if (responseObject.has("blockHash")) {
                return responseObject.getString("blockHash");
            } else {
                throw new RuntimeException("blockHash not found in the blockchain response.");
            }


        } catch (Exception e) {
            throw new RuntimeException("Error parsing blockchain response: " + e.getMessage());
        }
    }
}
