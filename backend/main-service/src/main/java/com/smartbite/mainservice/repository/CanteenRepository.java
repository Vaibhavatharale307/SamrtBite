package com.smartbite.mainservice.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartbite.mainservice.entity.Canteen;

public interface CanteenRepository extends JpaRepository<Canteen, Long> { 
	Optional<Canteen> findByCanteenName(String canteenName); 
	
}
