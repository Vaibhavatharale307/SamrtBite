package com.smartbite.walletservice.service;

import java.util.List;

import com.smartbite.walletservice.dto.MoneyRequest;
import com.smartbite.walletservice.dto.WalletRequest;
import com.smartbite.walletservice.dto.WalletResponse;
import com.smartbite.walletservice.dto.WalletTransactionResponse;

public interface WalletService {

	WalletResponse createWallet(WalletRequest request);

	WalletResponse getWallet(Long userId);

	WalletResponse addMoney(MoneyRequest request);

    WalletResponse deductMoney(MoneyRequest request);

    // Change 3: Transaction history
    List<WalletTransactionResponse> getTransactionHistory(Long userId);

}
