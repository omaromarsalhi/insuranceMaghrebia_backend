package com.maghrebia.payment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentIntentDto {

    private String paymentMethodId;

    private long amount;

    private String currency;


}
