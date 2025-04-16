package com.maghrebia.walletins.repository;

import com.maghrebia.walletins.entity.WalletTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends MongoRepository<WalletTransaction,String> {

    List<WalletTransaction> findByWalletId(String walletId);
}
