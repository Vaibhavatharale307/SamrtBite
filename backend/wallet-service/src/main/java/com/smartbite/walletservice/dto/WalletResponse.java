package com.smartbite.walletservice.dto;

import java.math.BigDecimal;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponse {

	private Long walletId;
	
	private Long userId;
	
	private BigDecimal balance;
}
