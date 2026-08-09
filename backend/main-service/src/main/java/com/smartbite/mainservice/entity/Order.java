package com.smartbite.mainservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "Orders") 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder

public class Order {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long orderId;
    
    private Long userId; 
    
    private Long canteenId;
    
    private Long foodId; 
    
    private Integer quantity;
    
    @Column(nullable = false)
    private String pickUpSlot;
    
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;
    
    @CreationTimestamp 
    private LocalDateTime createdAt;
}
