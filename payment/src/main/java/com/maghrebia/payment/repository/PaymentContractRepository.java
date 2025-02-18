package com.maghrebia.payment.repository;

import com.maghrebia.payment.entity.PaymentContract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentContractRepository extends MongoRepository<PaymentContract, String> {
}
