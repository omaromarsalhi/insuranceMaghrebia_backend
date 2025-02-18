package com.maghrebia.payment.repository;

import com.maghrebia.payment.entity.PaymentPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentPlanRepository extends MongoRepository<PaymentPlan, String> {
    List<PaymentPlan> findByPaymentContractId(String paymentContractId);

}