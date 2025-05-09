package com.maghrebia.payment.controller;

import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.service.PaymentPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/payment-plan")
public class PaymentPlanController {

    private final PaymentPlanService paymentPlanService;

    @PutMapping("/{id}")
    public ResponseEntity<PaymentPlan> updatePaymentPlan(@PathVariable String id, @RequestBody String hashblock ) {
        PaymentPlan updatedPayment = paymentPlanService.updatePaymentPlan(id,hashblock);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/{id}")
    public List<PaymentPlan> getAllPaymentsPlan(@PathVariable String id) {
        return paymentPlanService.getAllPaymentsPlan(id);

    }
}
