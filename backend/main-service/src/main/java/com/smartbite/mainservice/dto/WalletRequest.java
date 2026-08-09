package com.smartbite.mainservice.dto;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor 

public class WalletRequest {
	
	private Long userId; 
	
	private BigDecimal amount; 
	
}
