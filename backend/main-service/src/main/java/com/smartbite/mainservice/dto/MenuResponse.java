package com.smartbite.mainservice.dto;

import java.math.BigDecimal;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class MenuResponse {
	
	private Long foodId;
	
	private Long canteenId; 
	
	private String foodName;
	
	private String description;
	
	private BigDecimal price; 
	
	private String category; 
	
	private Boolean available; 
	
	private String imageUrl;
}
