package com.maghrebia.payment.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "Total amount must be a positive number")
    @NotNull(message = "Payment status cannot be null")
    private double totalAmount;

    @NotBlank(message = "Plan duration cannot be blank")
    private String planDuration;

    @NotBlank(message = "Payment status is required")
    @NotNull(message = "Payment status cannot be null")
    @Pattern(regexp = "^(Pending|Paid|Overdue)$", message = "Payment status must be either 'Pending', 'Paid', or 'Overdue'")
    private String paymentStatus;

    private boolean isArchived=false;

    @CreatedDate
    private Date contractCreatedAt;

    @LastModifiedDate
    private Date contractUpdatedAt;

    @DBRef
    private List<PaymentPlan> paymentPlans;


}
