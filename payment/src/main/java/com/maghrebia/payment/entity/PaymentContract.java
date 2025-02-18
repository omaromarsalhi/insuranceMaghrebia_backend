package com.maghrebia.payment.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "paymentContract")
public class PaymentContract {

    @Id
    private String contractPaymentId;

    private String userId;

    private String offerId;

    private double totalAmount;

    private String planDuration;

    private String paymentStatus;

    @CreatedDate
    private Date contractCreatedAt;

    @LastModifiedDate
    private Date contractUpdatedAt;

    @DBRef
    private List<PaymentPlan> paymentPlans;


}
