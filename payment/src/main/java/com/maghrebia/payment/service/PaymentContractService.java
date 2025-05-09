package com.maghrebia.payment.service;

import com.maghrebia.payment.dto.PaymentContractResponse;
import com.maghrebia.payment.entity.PaymentContract;
import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentMethod;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import com.maghrebia.payment.entity.enums.PlanDuration;
import com.maghrebia.payment.exceptions.InvalidAmountException;
import com.maghrebia.payment.exceptions.PaymentContractNotFoundException;
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




    public PaymentContractResponse createPaymentContract(PaymentContract contract, PaymentMethod paymentMethod) {
        contract.setContractCreatedAt(new Date());
        contract.setPaymentStatus(PaymentStatus.Pending);

        PaymentContract savedContract = paymentRepository.save(contract);

        savedContract.setPaymentPlans(generatePaymentPlans(
                savedContract.getTotalAmount(),
                savedContract.getPlanDuration(),
                savedContract.getContractCreatedAt(),
                savedContract.getContractPaymentId(),
                paymentMethod
        ));

         paymentRepository.save(savedContract);
        return PaymentContractResponse.builder()
                .contractPaymentId(savedContract.getContractPaymentId())
                .paymentPlans(savedContract.getPaymentPlans())
                .build();
//        return buildResponse(savedContract);
    }
    private List<PaymentPlan> generatePaymentPlans(double totalAmount, String planDuration,
                                                   Date createdAtDate, String paymentId,
                                                   PaymentMethod paymentMethod) {
        validateAmount(totalAmount);
        int numberOfMonths = getNumberOfMonths(planDuration);
        double amountDue = totalAmount / numberOfMonths;
        LocalDate createdAtLocalDate = convertToLocalDate(createdAtDate);

        List<PaymentPlan> paymentPlans = new ArrayList<>();

        for (int i = 1; i <= numberOfMonths; i++) {
            PaymentPlan plan = buildPaymentPlan(
                    i,
                    amountDue,
                    createdAtLocalDate.plusMonths(i),
                    paymentId,
                    paymentMethod == PaymentMethod.WALLET && i == 1
            );
            paymentPlans.add(plan);
        }

        return paymentPlanRepository.saveAll(paymentPlans);
    }

    private PaymentPlan buildPaymentPlan(int month, double amountDue, LocalDate dueDate,
                                         String paymentId, boolean markAsPaid) {
        PaymentPlan.PaymentPlanBuilder builder = PaymentPlan.builder()
                .month(month)
                .amountDue(amountDue)
                .paymentContractId(paymentId)
                .dueDate(convertToDate(dueDate));

        if (markAsPaid) {
            builder.amountPaid(amountDue)
                    .paymentStatus(PaymentStatus.Paid)
                    .paymentDate(new Date());
        } else {
            builder.amountPaid(0.0)
                    .paymentStatus(PaymentStatus.Pending)
                    .paymentDate(null);
        }

        return builder.build();
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
    public PaymentContract updatePaymentStatus(String id,String hashBlock) {

        PaymentContract payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));


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
        firstTranche.setHashBlock(hashBlock);

        allTranchesPaid = paymentPlans.stream()
                .allMatch(plan -> plan.getPaymentStatus() == PaymentStatus.Paid);


        if (allTranchesPaid) {
            payment.setPaymentStatus(PaymentStatus.Paid);
        }

        paymentPlanRepository.saveAll(paymentPlans);
        return paymentRepository.save(payment);
    }



    public void archivePaymentContract(PaymentContract paymentContract){
        PaymentContract archivedPaymentContract= paymentRepository.findById(paymentContract.getContractPaymentId())
        .orElseThrow(() -> new RuntimeException("Payment contract not found with ID: " + paymentContract.getContractPaymentId()));
        archivedPaymentContract.setArchived(true);
        paymentRepository.save(archivedPaymentContract);
    }
    private PaymentContractResponse buildResponse(PaymentContract contract) {
        return PaymentContractResponse.builder()
                .contractPaymentId(contract.getContractPaymentId())
                .paymentPlans(contract.getPaymentPlans())
                .build();
    }

    private int getNumberOfMonths(String planDuration) {
        return PlanDuration.fromLabel(planDuration).getMonths();
    }

    private void validateAmount(double totalAmount) {
        if (totalAmount <= 0) {
            throw new InvalidAmountException("Total amount must be greater than 0");
        }
    }
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }




}
