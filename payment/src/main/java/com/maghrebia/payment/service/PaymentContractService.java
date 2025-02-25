package com.maghrebia.payment.service;


import com.maghrebia.payment.config.StripeConfig;
import com.maghrebia.payment.dto.PaymentIntentDto;
import com.maghrebia.payment.dto.PaymentIntentResponse;
import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import com.maghrebia.payment.entity.enums.PlanDuration;
import com.maghrebia.payment.exceptions.InvalidAmountException;
import com.maghrebia.payment.repository.PaymentContractRepository;
import com.maghrebia.payment.repository.PaymentPlanRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@AllArgsConstructor
@Service
public class PaymentContractService {

    private  final PaymentContractRepository paymentRepository;

    private  final PaymentPlanRepository paymentPlanRepository;

    public PaymentContract createPaymentContract(PaymentContract contract) {
        contract.setContractCreatedAt(new Date());

        PaymentContract savedContract = paymentRepository.save(contract);

        savedContract.setPaymentPlans(generatePaymentPlans(
                savedContract.getTotalAmount(),
                savedContract.getPlanDuration(),
                savedContract.getContractCreatedAt(),
                savedContract.getContractPaymentId()
        ));

        return paymentRepository.save(savedContract);
    }

    public List<PaymentContract> getAllPayments() {
        return paymentRepository.findAllWithoutPaymentPlans();
    }

    public List<PaymentContract> getAllPaymentsDetails() {
        return paymentRepository.findAll();
    }


    public Optional<PaymentContract> getPaymentContractById(String id) {
        return paymentRepository.findById(id);
    }

    public PaymentContract updatePayment(String id, PaymentContract paymentDetails) {
        PaymentContract payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("payment not found with id: " + id));

        payment.setPlanDuration(paymentDetails.getPlanDuration());
        payment.setUserId(paymentDetails.getUserId());
        payment.setOfferId(paymentDetails.getOfferId());

        return paymentRepository.save(payment);
    }

    private List<PaymentPlan> generatePaymentPlans(double totalAmount, String planDuration, Date createdAtDate , String paymentId) {
        if (totalAmount <= 0) {
            throw new InvalidAmountException("Total amount must be greater than 0");        }
        List<PaymentPlan> paymentPlans = new ArrayList<>();
        int numberOfMonths = getNumberOfMonths(planDuration);
        double amountDue = totalAmount / numberOfMonths;

        LocalDate createdAtLocalDate = createdAtDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        for (int i = 1; i <= numberOfMonths; i++) {
            LocalDate dueDate = createdAtLocalDate.plusMonths(i);

            PaymentPlan plan = PaymentPlan.builder()
                    .month(i)
                    .amountDue(amountDue)
                    .amountPaid(i == 1 ? amountDue : 0.0)
                    .paymentStatus(i == 1 ? PaymentStatus.Paid : PaymentStatus.Pending)
                    .dueDate(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .paymentDate(i == 1 ? createdAtDate : null)
                    .paymentContractId(paymentId)
                    .build();

            paymentPlans.add(plan);
        }

        paymentPlanRepository.saveAll(paymentPlans);

        return paymentPlans;
    }

    private int getNumberOfMonths(String planDuration) {
        return PlanDuration.fromLabel(planDuration).getMonths();
    }

    public void archivePaymentContract(PaymentContract paymentContract){
        PaymentContract archivedPaymentContract= paymentRepository.findById(paymentContract.getContractPaymentId())
        .orElseThrow(() -> new RuntimeException("Payment contract not found with ID: " + paymentContract.getContractPaymentId()));
        archivedPaymentContract.setArchived(true);
        paymentRepository.save(archivedPaymentContract);
    }



}
