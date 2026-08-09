package com.smartbite.walletservice.config;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.smartbite.walletservice.entity.Wallet;
import com.smartbite.walletservice.entity.WalletTransaction;
import com.smartbite.walletservice.repository.WalletRepository;
import com.smartbite.walletservice.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletDataLoader implements CommandLineRunner {

    private final WalletRepository walletRepo;
    private final WalletTransactionRepository txnRepo;

    @Override
    public void run(String... args) {
        if (walletRepo.count() > 0) {
            log.info("Wallet data already loaded, skipping.");
            return;
        }

        log.info("Loading dummy wallet data...");

        // Create wallets for users (userId 1=admin, 2=manager1, 3=manager2, 4=student1, 5=student2, 6=student3)
        Wallet wallet4 = walletRepo.save(Wallet.builder().userId(4L).balance(new BigDecimal("1500.00")).build());
        Wallet wallet5 = walletRepo.save(Wallet.builder().userId(5L).balance(new BigDecimal("800.00")).build());
        Wallet wallet6 = walletRepo.save(Wallet.builder().userId(6L).balance(new BigDecimal("2200.00")).build());

        // Transaction history for student1 (userId=4)
        txnRepo.save(WalletTransaction.builder().userId(4L).amount(new BigDecimal("2000.00")).type("CREDIT").description("Money Added").build());
        txnRepo.save(WalletTransaction.builder().userId(4L).amount(new BigDecimal("90.00")).type("DEBIT").description("Order Payment").build());
        txnRepo.save(WalletTransaction.builder().userId(4L).amount(new BigDecimal("120.00")).type("DEBIT").description("Order Payment").build());
        txnRepo.save(WalletTransaction.builder().userId(4L).amount(new BigDecimal("290.00")).type("DEBIT").description("Order Payment").build());

        // Transaction history for student2 (userId=5)
        txnRepo.save(WalletTransaction.builder().userId(5L).amount(new BigDecimal("1000.00")).type("CREDIT").description("Money Added").build());
        txnRepo.save(WalletTransaction.builder().userId(5L).amount(new BigDecimal("80.00")).type("DEBIT").description("Order Payment").build());
        txnRepo.save(WalletTransaction.builder().userId(5L).amount(new BigDecimal("120.00")).type("DEBIT").description("Order Payment").build());

        // Transaction history for student3 (userId=6)
        txnRepo.save(WalletTransaction.builder().userId(6L).amount(new BigDecimal("2500.00")).type("CREDIT").description("Money Added").build());
        txnRepo.save(WalletTransaction.builder().userId(6L).amount(new BigDecimal("95.00")).type("DEBIT").description("Order Payment").build());
        txnRepo.save(WalletTransaction.builder().userId(6L).amount(new BigDecimal("95.00")).type("CREDIT").description("Order Refund").build());
        txnRepo.save(WalletTransaction.builder().userId(6L).amount(new BigDecimal("200.00")).type("DEBIT").description("Order Payment").build());

        log.info("Wallet dummy data loaded successfully!");
    }
}
