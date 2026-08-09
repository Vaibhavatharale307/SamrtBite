package com.smartbite.mainservice.entity;

import java.time.LocalTime;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "canteens") 
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Canteen {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long canteenId;
    
    @Column(nullable = false, unique = true)
    private String canteenName;
    
    private LocalTime openingTime;
    
    private LocalTime closingTime;
    
    private Boolean active;
    
    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Menu> menuItems;
}
