package com.maghrebia.payment.service;

import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.PaymentStatus;
import com.maghrebia.payment.exceptions.PaymentAlreadyCompletedException;
import com.maghrebia.payment.repository.PaymentPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentPlanService {

    private  final PaymentPlanRepository paymentPlanRepository;

    public PaymentPlan updatePaymentPlan(String id) {

            PaymentPlan paymentPlan = paymentPlanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("PaymentPlan not found with id: " + id));
            if(paymentPlan.getPaymentStatus()== PaymentStatus.Paid){
                throw new PaymentAlreadyCompletedException("Your payment for this tranche has been  already successfully completed.");
            }
            paymentPlan.setPaymentDate(new Date());
            paymentPlan.setPaymentStatus(PaymentStatus.Paid);
            paymentPlan.setAmountPaid(paymentPlan.getAmountDue());
            return paymentPlanRepository.save(paymentPlan);
        }

    public List<PaymentPlan> getAllPaymentsPlan(String contractId) {

       return paymentPlanRepository.findByPaymentContractId(contractId);
    }
}
