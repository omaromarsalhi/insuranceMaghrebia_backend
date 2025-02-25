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
    @PostMapping("confirm/{id}")
    public ResponseEntity<?> confirm(@PathVariable("id") String id) throws StripeException {
        PaymentIntent stripeClient = paymentStripetService.confirm(id);
        String paymentStr = stripeClient.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }
    @PostMapping("cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") String id) throws StripeException {
        PaymentIntent stripeClient = paymentStripetService.cancel(id);
        String paymentStr = stripeClient.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }
}
