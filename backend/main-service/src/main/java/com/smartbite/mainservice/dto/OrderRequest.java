package com.smartbite.mainservice.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter 
@ToString 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class OrderRequest { 
	
	@NotNull 
	private Long userId;
	
	@NotNull
	private Long canteenId; 
	
	@NotNull 
	private Long foodId; 
	
	@NotNull 
	@Min(1) 
	private Integer quantity;

	@NotBlank
	private String pickupSlot;
}
