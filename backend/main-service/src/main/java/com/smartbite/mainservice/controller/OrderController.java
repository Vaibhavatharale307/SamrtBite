package com.smartbite.mainservice.controller;

import java.util.List;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.smartbite.mainservice.dto.*;
import com.smartbite.mainservice.service.ManagerAccessService;
import com.smartbite.mainservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	
	
	private final OrderService orderservice;
	private final ManagerAccessService managerAccessService;

	@PostMapping
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest r) {
		
		return new ResponseEntity<>(orderservice.placeOrder(r), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		
		return ResponseEntity.ok(orderservice.getOrderById(id));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderResponse>> getOrderByUser(@PathVariable Long userId) {
		
		
		return ResponseEntity.ok(orderservice.getOrderByUser(userId));
	}

	
	@GetMapping("/canteen/{canteenId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CANTEEN_MANAGER')")
	public ResponseEntity<List<OrderResponse>> getOrdersByCanteen(@PathVariable Long canteenId) {
		managerAccessService.verifyCanteenAccess(canteenId);
		return ResponseEntity.ok(orderservice.getOrdersByCanteen(canteenId));
	}

	@PutMapping("/{orderId}/status")
	@PreAuthorize("hasAnyRole('ADMIN', 'CANTEEN_MANAGER')")
	public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,
			@RequestBody OrderStatusRequest r) {
		managerAccessService.verifyOrderAccess(orderId);
		return ResponseEntity.ok(orderservice.updateOrderStatus(orderId, r));
	}

	
	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
		
		return ResponseEntity.ok(orderservice.cancelOrder(orderId));
	}
}
