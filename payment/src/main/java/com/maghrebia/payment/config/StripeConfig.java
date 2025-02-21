package com.maghrebia.payment.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StripeConfig {

    @Value("${stripe.public-key}")
    private String publicKey;

    @Value("${stripe.secret-key}")
    private String secretKey;


    @PostConstruct
    public void initializeStripe() {
        Stripe.apiKey=secretKey;
        System.out.println("Stripe initialized with Public Key: " + publicKey);
    }
}
