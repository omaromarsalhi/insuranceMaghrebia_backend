package com.maghrebia.walletins.dto.mapper.response;

import com.maghrebia.walletins.entity.TransactionType;

import java.time.LocalDate;

public record WalletTransactionResponse(double amount,
                                        String description,
                                        TransactionType type,
                                        double reward,
                                        LocalDate contractCreatedAt,
                                        String walletId) {
}
