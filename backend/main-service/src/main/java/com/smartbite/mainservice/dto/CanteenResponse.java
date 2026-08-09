package com.smartbite.mainservice.dto;
import java.time.LocalTime;
import lombok.*;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CanteenResponse {
	
	private Long canteenId;
	
	private String canteenName;
	
	private LocalTime openingTime;
	
	private LocalTime closingTime; 
	
	private Boolean active; 
	
}
