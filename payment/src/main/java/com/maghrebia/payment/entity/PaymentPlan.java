package com.maghrebia.payment.entity;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Payment status cannot be null")
    @Positive(message = "Total amount must be a positive number")
    @Min(value = 50, message = "Total amount must be greater than 100")
    private double amountPaid;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "^(Pending|Paid|Overdue)$", message = "Payment status must be either 'Pending', 'Paid', or 'Overdue'")
    private PaymentStatus paymentStatus;

    private String paymentContractId;

    private Date paymentDate;

    private Date dueDate;

    private String hashBlock;


}
