package com.maghrebia.walletins.service;


import com.maghrebia.walletins.dto.mapper.WalletResponseMapper;
import com.maghrebia.walletins.dto.mapper.WalletTransactionMapper;
import com.maghrebia.walletins.entity.TransactionType;
import com.maghrebia.walletins.entity.Wallet;
import com.maghrebia.walletins.entity.WalletResponse;
import com.maghrebia.walletins.entity.WalletTransaction;
import com.maghrebia.walletins.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class WalletService {

    private  final WalletRepository walletRepository;

    public WalletResponse create(Wallet payload, double premiumPaid) {

        Wallet existingWallet = walletRepository.findWalletByUserId(payload.getUserId());
        if (existingWallet != null) {
            throw new RuntimeException("User Wallet Already created");
        }

        Wallet newWallet = createNewWallet(payload, premiumPaid);

        newWallet = walletRepository.save(newWallet);

        return buildWalletResponse(newWallet);
    }

    private void validateWalletPayload(Wallet payload , double premiumPaid) {
        if (payload == null) {
            throw new IllegalArgumentException("Wallet payload cannot be null");
        }
        if(premiumPaid < 0){
            throw new IllegalArgumentException("the amount cannot lesser than 0");

        }
        if (payload.getUserId() == null || payload.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }

    private Wallet createNewWallet(Wallet payload, double premiumPaid) {
        validateWalletPayload(payload,premiumPaid);


        Wallet existingWallet = walletRepository.findWalletByUserId(payload.getUserId());
        if (existingWallet != null) {
            throw new RuntimeException("User Wallet Already created");
        }

        double reward = premiumPaid * 0.05;
        if(payload.getSource()!=TransactionType.DEPOSIT)
        {
            throw new RuntimeException("Cannot Create Wallet");
        }
        return Wallet.builder()
                .source(payload.getSource())
                .userId(payload.getUserId())
                .fullName(payload.getFullName())
                .currency(payload.getCurrency())
                .balance(reward)
                .rewardsBalance(reward)
                .transactions(createInitialTransaction(payload, premiumPaid, reward))
                .build();
    }

    private List<WalletTransaction> createInitialTransaction(Wallet payload, double premiumPaid, double reward) {
        return List.of(WalletTransactionMapper.toTransaction(premiumPaid,reward,payload.getSource(),payload.getWalletId()));
    }


//    private WalletResponse buildWalletResponse(Wallet wallet) {
//        return WalletResponse.builder()
//                .balance(wallet.getBalance())
//                .currency(wallet.getCurrency())
//                .fullName(wallet.getFullName())
//                .rewardsBalance(wallet.getRewardsBalance())
//                .source(wallet.getSource())
//                .build();
//    }
    private WalletResponse buildWalletResponse(Wallet wallet) {
        return WalletResponseMapper.toWalletResponse(wallet);
    }


//    public WalletResponse create(Wallet payload, double premiumPaid) {
//        Wallet existingWallet = walletRepository.findWalletByUserId(payload.getUserId());
//        if (existingWallet!= null)
//        {
//            throw  new RuntimeException(" this is wallet exist ");
//        }
//        Wallet newWallet = (existingWallet != null) ? existingWallet : new Wallet();
//
//
//        double reward = premiumPaid * 0.05;
//        newWallet.setUserId(payload.getUserId());
//        newWallet.setSource(payload.getSource());
//        newWallet.setFullName(payload.getFullName());
//        newWallet.setCurrency(payload.getCurrency());
//        newWallet.setBalance(reward);
//        newWallet.setRewardsBalance(reward);
//
//
//        List<WalletTransaction> transactionList =new ArrayList<>();
//
//
//        TransactionType type = payload.getSource();
//        String description = "the transaction is comming from "+type;
//
//        WalletTransaction transaction = WalletTransaction
//                .builder()
//                .amount(premiumPaid)
//                .description(description)
//                .type(type)
//                .walletId(newWallet.getWalletId())
//                .reward(reward)
//                .build();
//        transactionList.add(transaction);
//        newWallet.setTransactions(transactionList);
//        System.out.println( " the new wallet : "+ newWallet);
//        walletRepository.save(newWallet);
//        return  WalletResponse.builder()
//                .userId(newWallet.getUserId())
//                .balance(newWallet.getBalance())
//                .currency(newWallet.getCurrency())
//                .fullName(newWallet.getFullName())
//                .source(newWallet.getSource())
//                .build();
//    }

    public WalletResponse update(String id,Wallet payload,double premiumPaid) {

        validateWalletPayload(payload,premiumPaid);

        Wallet foundWallet = walletRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Wallet not found"));

        double reward = premiumPaid * 0.05;
        double newReward = reward + foundWallet.getRewardsBalance();
        double newBalance = foundWallet.getBalance() + (payload.getSource() == TransactionType.REFUND ? premiumPaid : 0);

        TransactionType type = payload.getSource();

        List<WalletTransaction> transactionList =foundWallet.getTransactions() !=null?
                foundWallet.getTransactions():new ArrayList<>();


            WalletTransaction transaction = WalletTransactionMapper.toTransaction(premiumPaid,reward,type,id);
            transactionList.add(transaction);

        if (type == TransactionType.CLAIM_PAYOUT) {
            foundWallet.setBalance(newBalance);
            foundWallet.setRewardsBalance(newReward);

        } else if (type == TransactionType.PAYMENT) {
            if (premiumPaid > foundWallet.getBalance()) {
                throw new RuntimeException("Insufficient balance");
            }
            foundWallet.setBalance(foundWallet.getBalance() - premiumPaid);
        }

            foundWallet.setSource(payload.getSource());
            foundWallet.setTransactions(transactionList);

        walletRepository.save(foundWallet);

        return buildWalletResponse(foundWallet);
        }

    public List<WalletResponse> getAll() {
      return  walletRepository.findAll()
              .stream()
              .map(WalletResponseMapper::toWalletResponse)
              .toList();
    }
    public WalletResponse getOne(String userId) {
        Wallet foundWallet =walletRepository.findWalletByUserId(userId);
        if(foundWallet==null)
            throw new RuntimeException("Wallet Not Found");
        return WalletResponseMapper.toWalletResponse(foundWallet);
    }


}
