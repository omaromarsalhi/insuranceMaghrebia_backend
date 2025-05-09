package com.maghrebia.payment.repository;

import com.maghrebia.payment.entity.PaymentPlan;
import com.maghrebia.payment.entity.enums.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentPlanRepository extends MongoRepository<PaymentPlan, String> {
    List<PaymentPlan> findByPaymentContractId(String paymentContractId);

    List<PaymentPlan> findByDueDateBeforeAndPaymentStatus(Date date , PaymentStatus paymentStatus);

}