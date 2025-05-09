package com.maghrebia.walletins.repository;

import com.maghrebia.walletins.entity.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {

    Wallet findWalletByUserId(String id );

}