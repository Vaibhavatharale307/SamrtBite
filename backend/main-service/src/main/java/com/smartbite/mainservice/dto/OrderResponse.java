package com.smartbite.mainservice.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.smartbite.mainservice.entity.OrderStatus;
import lombok.*;

@Data
@Builder 
public class OrderResponse { 
	
	private Long orderId; 
	
	private Long userId; 
	
	private Long canteenId; 
	
	private Long foodId; 
	
	private Integer quantity;
	
	private BigDecimal totalAmount; 
	
	private OrderStatus status; 
	
	private LocalDateTime createdAt; 
	
	private String pickupSlot;
	
}
