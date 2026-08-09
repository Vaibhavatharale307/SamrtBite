package com.smartbite.mainservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartbite.mainservice.entity.Menu;


public interface MenuRepository extends JpaRepository<Menu, Long> {
	
	List<Menu> findByCanteen_CanteenId(Long canteenId); 
	
}
