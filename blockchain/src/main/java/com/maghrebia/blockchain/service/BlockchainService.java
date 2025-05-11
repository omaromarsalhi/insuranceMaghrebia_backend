package com.maghrebia.blockchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maghrebia.blockchain.dto.Blockchain;
import com.maghrebia.blockchain.dto.PaymentBlockRequestDto;
import com.maghrebia.blockchain.dto.PaymentBlockResponseDto;
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
    private final String BLOCKCHAIN_API_URL = "https://de73-41-226-79-183.ngrok-free.app/blockchain";

    public Blockchain getPayment(String paymentId) {
        // Update the URL to use paymentId instead of index
        String url = BLOCKCHAIN_API_URL + "/get-payment/" + paymentId;

        try {
            // Sending a GET request to the blockchain API
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Optionally handle a null response if required
            // if (response == null) {
            //     throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blockchain record not found for paymentId: " + paymentId);
            // }

            // Use ObjectMapper to deserialize the response body into the Blockchain object
            ObjectMapper objectMapper = new ObjectMapper();
            Blockchain blockchain = objectMapper.readValue(response.getBody(), Blockchain.class);

            // Return the deserialized Blockchain object
            return blockchain;

        } catch (Exception e) {
            // Log the error and throw a runtime exception if there is an issue with the response
            System.out.println("Error parsing blockchain response: " + e.getMessage());
            throw new RuntimeException("Error parsing blockchain response: " + e.getMessage());
        }
    }

    public PaymentBlockResponseDto addPayment(PaymentBlockRequestDto blockchain) {
        String url = BLOCKCHAIN_API_URL + "/add-payment/";

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

            if (responseObject.has("transactionHash") && responseObject.has("blockHash") && responseObject.has("status")) {

                PaymentBlockResponseDto transactionResponse = new PaymentBlockResponseDto();
                transactionResponse.setTransactionHash(responseObject.getString("transactionHash"));
                transactionResponse.setBlockHash(responseObject.getString("blockHash"));
                transactionResponse.setStatus(responseObject.getString("status"));

                return transactionResponse;
            } else {
                throw new RuntimeException("Missing expected fields in blockchain response.");
            }


        } catch (Exception e) {
            throw new RuntimeException("Error parsing blockchain response: " + e.getMessage());
        }
    }
}
