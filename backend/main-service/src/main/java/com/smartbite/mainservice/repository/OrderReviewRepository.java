package com.smartbite.mainservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartbite.mainservice.entity.OrderReview;

public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {
    List<OrderReview> findByFoodId(Long foodId);
    List<OrderReview> findByUserId(Long userId);
    Optional<OrderReview> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}
