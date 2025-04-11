package com.maghrebia.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBlockRequestDto {

    private String paymentId;

    private Long amount;

    private String fullname;
}
