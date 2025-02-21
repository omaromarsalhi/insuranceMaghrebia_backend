package com.maghrebia.payment.repository;

import com.maghrebia.payment.entity.PaymentContract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentContractRepository extends MongoRepository<PaymentContract, String> {
    @Query(value = "{}", fields = "{ 'paymentPlans' : 0 }")
    List<PaymentContract> findAllWithoutPaymentPlans();
}
