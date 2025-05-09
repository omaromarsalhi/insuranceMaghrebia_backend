package com.maghrebia.walletins.dto.mapper;

import com.maghrebia.walletins.entity.Wallet;
import com.maghrebia.walletins.entity.WalletResponse;

import java.util.List;

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
    public static WalletResponse toWalletResponse(Wallet wallet, boolean includeTransactions) {
        return WalletResponse.builder()
                .walletId(wallet.getWalletId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .fullName(wallet.getFullName())
                .rewardsBalance(wallet.getRewardsBalance())
                .source(wallet.getSource())
                .transactions(includeTransactions ? wallet.getTransactions() : List.of())
                .build();
    }

}
