package com.maghrebia.payment.controller;

import com.maghrebia.payment.dto.PaymentContractResponse;
import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.entity.enums.PaymentMethod;
import com.maghrebia.payment.service.PaymentContractService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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
        public ResponseEntity<PaymentContractResponse>  createContract(
                @Valid @RequestBody PaymentContract contract,
                @RequestParam(required = true)PaymentMethod paymentMethod
                ) {
            PaymentContractResponse paymentContractId = paymentContractService.createPaymentContract(contract,paymentMethod);
            return ResponseEntity.ok(paymentContractId);
        }

    @GetMapping("/Payments")
    public List<PaymentContract> getAllPayments() {
        return paymentContractService.getAllPayments();
    }

    @GetMapping
    public List<PaymentContract> getAllPaymentDetails() {
        return paymentContractService.getAllPaymentsDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentContract> getPaymentContractById(@PathVariable String id) {
        return paymentContractService.getPaymentContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PaymentContract>> getPaymentContractByUserId(@PathVariable String id) {
        List<PaymentContract> contracts = paymentContractService.getAllPaymentByUserId(id);
        return ResponseEntity.ok(contracts);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaymentContract(@PathVariable String id,@RequestBody String hashBlock) {
        paymentContractService.updatePaymentStatus(id,hashBlock);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\": \"Payment contract status updated successfully.\"}");

    }


//    @PutMapping("/archive")
//    public ResponseEntity<String> archivePaymentContract(@RequestBody PaymentContract paymentContract) {
//        try {
//            paymentContractService.archivePaymentContract(paymentContract);
//            return ResponseEntity.ok("Payment contract archived successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to archive payment contract");
//        }
//    }

}