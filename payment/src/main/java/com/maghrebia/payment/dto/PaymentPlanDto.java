package com.maghrebia.payment.dto;

import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Builder
public record PaymentPlanDto(
        int month,
        double amountDue,
        double amountPaid,
        PaymentStatus paymentStatus,
        LocalDate dueDate,
        LocalDate paymentDate,
        String paymentContractId
) {
    public PaymentPlan toEntity() {
        return PaymentPlan.builder()
                .month(month)
                .amountDue(amountDue)
                .amountPaid(amountPaid)
                .paymentStatus(paymentStatus)
                .dueDate(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .paymentDate(paymentDate != null ?
                        Date.from(paymentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null)
                .paymentContractId(paymentContractId)
                .build();
    }
}