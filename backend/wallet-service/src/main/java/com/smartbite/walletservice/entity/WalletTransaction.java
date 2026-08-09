package com.smartbite.walletservice.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallet_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    private Long userId;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    // CREDIT = paise aaye, DEBIT = paise gaye
    private String type;

    // Reason: "Money Added", "Order Payment", "Order Refund"
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
