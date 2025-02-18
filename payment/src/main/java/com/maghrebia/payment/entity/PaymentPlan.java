package com.maghrebia.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "paymentPlan")
public class PaymentPlan {

    @Id
    private String paymentPlanId;

    private int month;

    private double amountDue;

    private double amountPaid;

    private PaymentStatus paymentStatus;

    private String paymentContractId;


    private Date paymentDate;

    private Date dueDate;

    private String hashBlock;


}
