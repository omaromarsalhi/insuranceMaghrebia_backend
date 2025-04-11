package com.maghrebia.payment.config;

import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import com.maghrebia.payment.repository.PaymentPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class PaymentDueDateChecker {

    private PaymentPlanRepository paymentPlanRepository;

    @Scheduled(cron = "0/2 0 * * * ?")
    public void checkDuePayments() {
        LocalDate today = LocalDate.now();
        Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<PaymentPlan> overduePlans = paymentPlanRepository.findByDueDateBeforeAndPaymentStatus(
                todayDate,
                PaymentStatus.Pending
        );

        for (PaymentPlan plan : overduePlans) {
            plan.setPaymentStatus(PaymentStatus.Overdue);
            paymentPlanRepository.save(plan);
        }
    }
}