package com.maghrebia.walletins.dto.mapper;

import com.maghrebia.walletins.entity.Wallet;
import com.maghrebia.walletins.entity.WalletResponse;

public class WalletResponseMapper {
    public static WalletResponse toWalletResponse(Wallet wallet) {
        return WalletResponse.builder()
                .walletId(wallet.getWalletId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .fullName(wallet.getFullName())
                .rewardsBalance(wallet.getRewardsBalance())
                .source(wallet.getSource())
                .transactions(wallet.getTransactions())
                .build();
    }

}
