package com.maghrebia.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBlockResponseDto {

    private String transactionHash;

    private String blockHash;

    private String status;
}
