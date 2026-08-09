package com.smartbite.walletservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartbite.walletservice.dto.MoneyRequest;
import com.smartbite.walletservice.dto.WalletRequest;
import com.smartbite.walletservice.dto.WalletResponse;
import com.smartbite.walletservice.dto.WalletTransactionResponse;
import com.smartbite.walletservice.entity.Wallet;
import com.smartbite.walletservice.entity.WalletTransaction;
import com.smartbite.walletservice.exception.BadRequestException;
import com.smartbite.walletservice.exception.InsufficientBalanceException;
import com.smartbite.walletservice.exception.ResourceNotFoundException;
import com.smartbite.walletservice.repository.WalletRepository;
import com.smartbite.walletservice.repository.WalletTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepo;
    private final WalletTransactionRepository txnRepo;

    @Override
    @Transactional
    public WalletResponse createWallet(WalletRequest request) {
        if (walletRepo.findByUserId(request.getUserId()).isPresent()) {
            throw new BadRequestException("Wallet already exists for this user");
        }

        Wallet wallet = walletRepo.save(Wallet.builder()
                .userId(request.getUserId())
                .balance(BigDecimal.ZERO)
                .build());

        return mapToResponse(wallet);
    }

    @Override
    public WalletResponse getWallet(Long userId) {
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for userId: " + userId));
        return mapToResponse(wallet);
    }

    @Override
    public WalletResponse addMoney(MoneyRequest request) {
        return retryWalletUpdate(() -> addMoneyOnce(request));
    }

    @Transactional
    protected WalletResponse addMoneyOnce(MoneyRequest request) {
        Wallet wallet = walletRepo.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        BigDecimal balance = wallet.getBalance() == null ? BigDecimal.ZERO : wallet.getBalance();
        wallet.setBalance(balance.add(request.getAmount()));
        Wallet updated = walletRepo.saveAndFlush(wallet);

        txnRepo.save(WalletTransaction.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .type("CREDIT")
                .description("Money Added")
                .build());

        log.info("Wallet credited for user {} amount {}", request.getUserId(), request.getAmount());
        return mapToResponse(updated);
    }

    @Override
    public WalletResponse deductMoney(MoneyRequest request) {
        return retryWalletUpdate(() -> deductMoneyOnce(request));
    }

    @Transactional
    protected WalletResponse deductMoneyOnce(MoneyRequest request) {
        Wallet wallet = walletRepo.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs. " + wallet.getBalance() + ", Required: Rs. " + request.getAmount());
        }

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        Wallet updated = walletRepo.saveAndFlush(wallet);

        txnRepo.save(WalletTransaction.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .type("DEBIT")
                .description("Order Payment")
                .build());

        log.info("Wallet debited for user {} amount {}", request.getUserId(), request.getAmount());
        return mapToResponse(updated);
    }

    @Override
    public List<WalletTransactionResponse> getTransactionHistory(Long userId) {
        return txnRepo.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(txn -> WalletTransactionResponse.builder()
                        .txnId(txn.getTxnId())
                        .userId(txn.getUserId())
                        .amount(txn.getAmount())
                        .type(txn.getType())
                        .description(txn.getDescription())
                        .createdAt(txn.getCreatedAt())
                        .build())
                .toList();
    }

    private WalletResponse retryWalletUpdate(WalletUpdateAction action) {
        ObjectOptimisticLockingFailureException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return action.execute();
            } catch (ObjectOptimisticLockingFailureException ex) {
                lastException = ex;
                log.warn("Wallet update conflict. Retrying attempt {}", attempt);
            }
        }
        throw lastException;
    }

    private WalletResponse mapToResponse(Wallet wallet) {
        return WalletResponse.builder()
                .walletId(wallet.getWalletId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .build();
    }

    private interface WalletUpdateAction {
        WalletResponse execute();
    }
}
