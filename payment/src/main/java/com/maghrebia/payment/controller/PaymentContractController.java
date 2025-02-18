package com.maghrebia.payment.controller;

import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.service.PaymentContractService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payment-contracts")
public class PaymentContractController {

    private final PaymentContractService paymentContractService;

    @PostMapping
    public PaymentContract createContract(@RequestBody PaymentContract contract) {
        return paymentContractService.createPaymentContract(contract);
    }

    @GetMapping
    public List<PaymentContract> getAllPayments() {
        return paymentContractService.getAllPayments();
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
            @RequestBody PaymentContract paymentDetails)
    {
        PaymentContract updatedPayment = paymentContractService.updatePayment(id, paymentDetails);
        return ResponseEntity.ok(updatedPayment);
    }

}
