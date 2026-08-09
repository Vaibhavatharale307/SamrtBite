package com.smartbite.walletservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbite.walletservice.entity.WalletTransaction;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    // userId ke saare transactions (latest first)
    List<WalletTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}
