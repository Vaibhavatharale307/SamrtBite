package com.smartbite.walletservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.smartbite.walletservice.dto.MoneyRequest;
import com.smartbite.walletservice.entity.Wallet;
import com.smartbite.walletservice.entity.WalletTransaction;
import com.smartbite.walletservice.exception.InsufficientBalanceException;
import com.smartbite.walletservice.repository.WalletRepository;
import com.smartbite.walletservice.repository.WalletTransactionRepository;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionRepository transactionRepository;

    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletService = new WalletServiceImpl(walletRepository, transactionRepository);
    }

    @Test
    void deductMoneyWithInsufficientBalanceThrows() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet(new BigDecimal("20.00"))));

        MoneyRequest request = MoneyRequest.builder()
                .userId(1L)
                .amount(new BigDecimal("50.00"))
                .build();

        assertThrows(InsufficientBalanceException.class, () -> walletService.deductMoney(request));
    }

    @Test
    void addMoneyIncreasesBalanceAndCreatesTransaction() {
        Wallet wallet = wallet(new BigDecimal("100.00"));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.saveAndFlush(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MoneyRequest request = MoneyRequest.builder()
                .userId(1L)
                .amount(new BigDecimal("50.00"))
                .build();

        assertEquals(new BigDecimal("150.00"), walletService.addMoney(request).getBalance());
        verify(transactionRepository).save(any(WalletTransaction.class));
    }

    @Test
    void addMoneyRetriesOnceAfterOptimisticLockConflict() {
        Wallet firstAttempt = wallet(new BigDecimal("100.00"));
        Wallet secondAttempt = wallet(new BigDecimal("100.00"));
        when(walletRepository.findByUserId(1L))
                .thenReturn(Optional.of(firstAttempt))
                .thenReturn(Optional.of(secondAttempt));
        when(walletRepository.saveAndFlush(any(Wallet.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Wallet.class, 1L))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MoneyRequest request = MoneyRequest.builder()
                .userId(1L)
                .amount(new BigDecimal("25.00"))
                .build();

        assertEquals(new BigDecimal("125.00"), walletService.addMoney(request).getBalance());
    }

    private Wallet wallet(BigDecimal balance) {
        return Wallet.builder()
                .walletId(1L)
                .userId(1L)
                .balance(balance)
                .build();
    }
}
