package com.smartbite.walletservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.smartbite.walletservice.dto.MoneyRequest;
import com.smartbite.walletservice.dto.WalletRequest;
import com.smartbite.walletservice.dto.WalletResponse;
import com.smartbite.walletservice.dto.WalletTransactionResponse;
import com.smartbite.walletservice.service.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletservice;

    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest request) {
    	
        return ResponseEntity.ok(walletservice.createWallet(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long userId) {
    	
        return ResponseEntity.ok(walletservice.getWallet(userId));
    }

    @PutMapping("/addMoney")
    public ResponseEntity<WalletResponse> addMoney(@Valid @RequestBody MoneyRequest request) {
    	
        return ResponseEntity.ok(walletservice.addMoney(request));
    }

    @PutMapping("/deductMoney")
    public ResponseEntity<WalletResponse> deductMoney(@Valid @RequestBody MoneyRequest request) {
    	
        return ResponseEntity.ok(walletservice.deductMoney(request));
    }
    

    // Change 3: Transaction History endpoint
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<WalletTransactionResponse>> getHistory(@PathVariable Long userId) {
    	
        return ResponseEntity.ok(walletservice.getTransactionHistory(userId));
    }

}
