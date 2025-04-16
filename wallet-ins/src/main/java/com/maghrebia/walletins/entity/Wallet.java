package com.maghrebia.walletins.entity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "Wallet")
public class Wallet {
    @Id
    private String walletId;

    private String userId;

    private double balance;

    private String currency;

    private double rewardsBalance;

    private String fullName;

    private TransactionType source;

    private boolean isArchived=false;

    @CreatedDate
    private LocalDate contractCreatedAt;

    private LocalDate updated_at;

    private List<WalletTransaction> transactions;

}
