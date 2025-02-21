package com.maghrebia.payment.controller;

import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.service.PaymentContractService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/payment-contracts")
public class PaymentContractController {

    private final PaymentContractService paymentContractService;

    @PostMapping
    public PaymentContract createContract(@Valid @RequestBody PaymentContract contract) {
        return paymentContractService.createPaymentContract(contract);
    }

    @GetMapping("/Payments")
    public List<PaymentContract> getAllPayments() {
        return paymentContractService.getAllPayments();
    }

    @GetMapping()
    public List<PaymentContract> getAllPaymentDetails() {
        return paymentContractService.getAllPaymentsDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentContract> getPaymentContractById(@PathVariable String id) {
        return paymentContractService.getPaymentContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentContract> updatePaymentContract(
            @PathVariable String id,
            @Valid @RequestBody PaymentContract paymentDetails)
    {
        PaymentContract updatedPayment = paymentContractService.updatePayment(id, paymentDetails);
        return ResponseEntity.ok(updatedPayment);
    }
    @PostMapping("/create-checkout-session")
    public String createCheckoutSession() throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8099/api/v1/payment-contracts/success")
                .setCancelUrl("http://localhost:8099/api/v1/payment-contracts/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(100L)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Test Product")
                                                                .build()
                                                )
                                                .build()
                                ).setQuantity(1L)
                                .build()
                )
                .build();
        Session session = Session.create(params);
        return session.getId();
    }

    @GetMapping("/success")
    public String getSuccess(){
        return "payment successful";
    }
    @GetMapping("/cancel")
    public String cancel(){
        return "payment canceled";
    }

}
