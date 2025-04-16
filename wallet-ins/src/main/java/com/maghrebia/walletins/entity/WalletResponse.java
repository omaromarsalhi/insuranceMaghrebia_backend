package com.maghrebia.walletins.entity;

import lombok.Builder;

import java.util.List;

@Builder
public record WalletResponse(

         String walletId,

         String userId,

         double balance,

         String currency,

         String fullName,
        double rewardsBalance,

         TransactionType source,

         List<WalletTransaction> transactions
) {
}
