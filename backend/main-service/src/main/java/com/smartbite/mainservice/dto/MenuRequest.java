package com.smartbite.mainservice.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class MenuRequest { 
	
	@NotNull(message = "Canteen ID is required")
	private Long canteenId;
	
	@NotBlank(message = "Food name is required")
	private String foodName;
	
	private String description;
	
	@NotNull(message = "Price is required")
	private BigDecimal price; 
	
	private String category; 
	
	private Boolean available; 
	
	private String imageUrl;
}
