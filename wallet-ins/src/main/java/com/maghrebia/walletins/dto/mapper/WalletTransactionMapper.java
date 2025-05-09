package com.maghrebia.walletins.dto.mapper;

import com.maghrebia.walletins.entity.TransactionType;
import com.maghrebia.walletins.entity.WalletTransaction;

import java.time.LocalDate;


public class WalletTransactionMapper {
    public static WalletTransaction toTransaction(
             double amount,
             double reward,
             TransactionType type,
             String walletId
    ) {
        return WalletTransaction.builder()
                .walletId(walletId)
                .amount(amount)
                .type(type)
                .reward(reward)
                .description("Transaction from " + type)
                .contractCreatedAt(LocalDate.now())
                .build();
    }
}
