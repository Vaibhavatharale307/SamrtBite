package com.smartbite.walletservice.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletTransactionResponse {

    private Long txnId;
    private Long userId;
    private BigDecimal amount;
    private String type;          // CREDIT / DEBIT
    private String description;   // "Money Added" / "Order Payment" / "Order Refund"
    private LocalDateTime createdAt;
}
