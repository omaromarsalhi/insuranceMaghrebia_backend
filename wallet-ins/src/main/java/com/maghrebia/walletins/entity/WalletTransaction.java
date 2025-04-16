package com.maghrebia.walletins.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "WalletTransaction")
public class WalletTransaction {

    @Id
    private String walletTransactionId;

    private double amount;

    private double reward;

    private String description;

    private TransactionType type;

    private String walletId;

    @CreatedDate
    private LocalDate contractCreatedAt;



}
