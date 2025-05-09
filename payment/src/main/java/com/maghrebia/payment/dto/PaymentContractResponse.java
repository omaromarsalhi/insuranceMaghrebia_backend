package com.maghrebia.payment.dto;

import com.maghrebia.payment.entity.PaymentPlan;
import lombok.Builder;

import java.util.List;

@Builder
public record PaymentContractResponse(
        String contractPaymentId,
        List<PaymentPlan> paymentPlans
) {
}
