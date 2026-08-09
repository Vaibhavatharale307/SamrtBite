package com.smartbite.mainservice.repository;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smartbite.mainservice.entity.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> { 
	
	List<Order> findByUserId(Long id);
	
	List<Order> findByCanteenId(Long id);
	
	long countByPickUpSlot(String pickupSlot);
	
}
