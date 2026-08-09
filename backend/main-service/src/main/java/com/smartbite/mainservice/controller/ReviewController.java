package com.smartbite.mainservice.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smartbite.mainservice.dto.ReviewRequest;
import com.smartbite.mainservice.dto.ReviewResponse;
import com.smartbite.mainservice.entity.Order;
import com.smartbite.mainservice.entity.OrderReview;
import com.smartbite.mainservice.entity.OrderStatus;
import com.smartbite.mainservice.exception.BadRequestException;
import com.smartbite.mainservice.exception.ResourceNotFoundException;
import com.smartbite.mainservice.repository.OrderRepository;
import com.smartbite.mainservice.repository.OrderReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final OrderReviewRepository reviewRepo;
    private final OrderRepository orderRepo;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<ReviewResponse> submitReview(@PathVariable Long orderId,
                                                        @Valid @RequestBody ReviewRequest request) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.COMPLETED)
            throw new BadRequestException("Reviews can only be submitted for COMPLETED orders");

        if (reviewRepo.existsByOrderId(orderId))
            throw new BadRequestException("Review already submitted for this order");

        OrderReview review = reviewRepo.save(OrderReview.builder()
                .orderId(orderId)
                .userId(order.getUserId())
                .foodId(order.getFoodId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build());

        return ResponseEntity.ok(mapToResponse(review));
    }

    @GetMapping("/food/{foodId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByFood(@PathVariable Long foodId) {
        return ResponseEntity.ok(reviewRepo.findByFoodId(foodId).stream().map(this::mapToResponse).toList());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewRepo.findByUserId(userId).stream().map(this::mapToResponse).toList());
    }

    private ReviewResponse mapToResponse(OrderReview r) {
        return ReviewResponse.builder()
                .reviewId(r.getReviewId())
                .orderId(r.getOrderId())
                .userId(r.getUserId())
                .foodId(r.getFoodId())
                .rating(r.getRating())
                .comment(r.getComment())
                .reviewedAt(r.getReviewedAt())
                .build();
    }
}
