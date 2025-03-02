package com.maghrebia.payment.controller;

import com.maghrebia.payment.dto.PaymentIntentDto;
import com.maghrebia.payment.dto.PaymentIntentResponse;
import com.maghrebia.payment.service.PaymentStripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payment-Intent")
public class PaymentIntentController {

    private final PaymentStripeService paymentStripetService;

    @PostMapping("paymentIntent")
    public ResponseEntity<PaymentIntentResponse> payment(@RequestBody PaymentIntentDto paymentIntent) throws StripeException {
        PaymentIntentResponse paymentResponse = paymentStripetService.createPaymentIntent(paymentIntent);
        return ResponseEntity.ok(paymentResponse);
    }
    @PostMapping("/capture-payment/{paymentIntentId}")
    public ResponseEntity<Map<String, String>> capturePayment(@PathVariable String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        paymentIntent.capture(); // Capture the authorized payment
        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Payment captured successfully."));
    }

    @PostMapping("/cancel-payment/{paymentIntentId}")
    public ResponseEntity<Map<String, String>> cancelPayment(@PathVariable String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        paymentIntent.cancel(); // Cancel the authorization
        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Payment authorization canceled."));
    }
}
