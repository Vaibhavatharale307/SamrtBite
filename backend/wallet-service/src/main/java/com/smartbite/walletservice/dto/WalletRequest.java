package com.smartbite.walletservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @data annotation is shortcut for
 * @Getter
   @Setter
   @ToString
   @EqualsAndHashCode
   @RequiredArgsConstructor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRequest {
	
	
	private Long userId;
	
	private BigDecimal balance;
	
	

}
