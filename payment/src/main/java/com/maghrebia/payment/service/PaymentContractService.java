package com.maghrebia.payment.service;

import com.maghrebia.payment.entity.IndexTracker;
import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import com.maghrebia.payment.entity.enums.PlanDuration;
import com.maghrebia.payment.exceptions.InvalidAmountException;
import com.maghrebia.payment.exceptions.PaymentContractNotFoundException;
import com.maghrebia.payment.repository.IndexTrackerRepository;
import com.maghrebia.payment.repository.PaymentContractRepository;
import com.maghrebia.payment.repository.PaymentPlanRepository;
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

    private final IndexTrackerRepository indexTrackerRepository;

    public String createPaymentContract(PaymentContract contract) {
        contract.setContractCreatedAt(new Date());
        contract.setPaymentStatus(PaymentStatus.Pending);

        PaymentContract savedContract = paymentRepository.save(contract);

        savedContract.setPaymentPlans(generatePaymentPlans(
                savedContract.getTotalAmount(),
                savedContract.getPlanDuration(),
                savedContract.getContractCreatedAt(),
                savedContract.getContractPaymentId()
        ));

         paymentRepository.save(savedContract);
        return savedContract.getContractPaymentId();
    }

    public List<PaymentContract> getAllPayments() {
        return paymentRepository.findAllWithoutPaymentPlans();
    }

    public List<PaymentContract> getAllPaymentsDetails() {
        return paymentRepository.findAll();
    }

    public List<PaymentContract> getAllPaymentByUserId(String userId) {
        List<PaymentContract> contracts = paymentRepository.findByUserId(userId);

        if (contracts.isEmpty())
            throw new PaymentContractNotFoundException("No payment contracts found for userId: " + userId);
        return contracts;
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
    public PaymentContract updatePaymentStatus(String id) {

        PaymentContract payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        IndexTracker indexTracker = indexTrackerRepository.findById("payment_plan_index")
                .orElseThrow(() -> new RuntimeException("Index tracker not found"));
        int newIndex = indexTracker.getIndex() + 1;


        List<PaymentPlan> paymentPlans = paymentPlanRepository.findByPaymentContractId(id);
        if (paymentPlans.isEmpty()) {
            throw new RuntimeException("No PaymentPlans found for PaymentContract with id: " + id);
        }

        boolean allTranchesPaid = paymentPlans.stream()
                .allMatch(plan -> plan.getPaymentStatus() == PaymentStatus.Paid);

        if (allTranchesPaid) {
            throw new RuntimeException("All payments have already been made. No further payment is required.");
        }

        PaymentPlan firstTranche = paymentPlans.get(0);

        if (firstTranche.getPaymentStatus() == PaymentStatus.Paid) {
            throw new RuntimeException("The first tranche is already paid. No further payment is required.");
        }

        firstTranche.setPaymentStatus(PaymentStatus.Paid);
        firstTranche.setAmountPaid(firstTranche.getAmountDue());
        firstTranche.setPaymentDate(new Date());
        firstTranche.setHashBlock(firstTranche.getHashBlock());
        firstTranche.setIndex(newIndex);

        allTranchesPaid = paymentPlans.stream()
                .allMatch(plan -> plan.getPaymentStatus() == PaymentStatus.Paid);


        if (allTranchesPaid) {
            payment.setPaymentStatus(PaymentStatus.Paid);
        }

        paymentPlanRepository.saveAll(paymentPlans);
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
                    .amountPaid(0.0)
                    .paymentStatus(PaymentStatus.Pending)
                    .dueDate(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .paymentDate(null)
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
