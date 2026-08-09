package com.smartbite.mainservice.service;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import com.smartbite.mainservice.dto.*;
import com.smartbite.mainservice.entity.Order;
import com.smartbite.mainservice.entity.OrderStatus;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.exception.BadRequestException;
import com.smartbite.mainservice.exception.FoodUnavailableException;
import com.smartbite.mainservice.exception.OrderCancellationException;
import com.smartbite.mainservice.exception.PaymentException;
import com.smartbite.mainservice.exception.ResourceNotFoundException;
import com.smartbite.mainservice.exception.SlotFullException;
import com.smartbite.mainservice.repository.OrderRepository;
import com.smartbite.mainservice.security.CurrentUserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepo;
    private final CanteenService canteenService;
    private final RestClient walletRestClient;
    private final CurrentUserService currentUserService;
    private final ManagerAccessService managerAccessService;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        User loggedInUser = currentUserService.getCurrentUser();
        if (!loggedInUser.getUserId().equals(request.getUserId())) {
            throw new AccessDeniedException("You can place order only for your own account");
        }

        MenuResponse menu = canteenService.getMenuByFoodId(request.getFoodId());

        if (!menu.getAvailable())
            throw new FoodUnavailableException("Food item '" + menu.getFoodName() + "' is currently not available");

        if (!menu.getCanteenId().equals(request.getCanteenId())) {
            throw new BadRequestException("Selected food item does not belong to this canteen");
        }

        BigDecimal totalAmount = menu.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        if (orderRepo.countByPickUpSlot(request.getPickupSlot()) >= 20)
            throw new SlotFullException("Pickup slot '" + request.getPickupSlot() + "' is full. Please choose another slot.");

        WalletRequest walletRequest = WalletRequest.builder()
                .userId(request.getUserId())
                .amount(totalAmount)
                .build();

        Order savedOrder = orderRepo.save(Order.builder()
                .userId(request.getUserId())
                .canteenId(request.getCanteenId())
                .foodId(request.getFoodId())
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING_PAYMENT)
                .pickUpSlot(request.getPickupSlot())
                .build());

        try {
            walletRestClient.put()
                    .uri("/wallet/deductMoney")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(walletRequest)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepo.save(savedOrder);
            log.error("Wallet deduction failed for order {}: {}", savedOrder.getOrderId(), ex.getResponseBodyAsString());
            throw new PaymentException("Payment failed: " + ex.getResponseBodyAsString());
        }

        savedOrder.setStatus(OrderStatus.PLACED);
        savedOrder = saveWithRetry(savedOrder);
        log.info("Order {} placed for user {}", savedOrder.getOrderId(), savedOrder.getUserId());

        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        verifyOrderReadAccess(order);
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrderByUser(Long id) {
        User loggedInUser = currentUserService.getCurrentUser();
        List<Order> orders = orderRepo.findByUserId(id);
        if (loggedInUser.getRole().getRoleName() == RoleType.ADMIN || loggedInUser.getUserId().equals(id)) {
            return orders.stream().map(this::mapToResponse).toList();
        }
        for (Order order : orders) {
            managerAccessService.verifyCanteenAccess(order.getCanteenId());
        }
        return orders.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<OrderResponse> getOrdersByCanteen(Long id) {
        return orderRepo.findByCanteenId(id).stream().map(this::mapToResponse).toList();
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatusRequest request) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (request.getStatus() == null)
            throw new BadRequestException("Order status is required");

        if (!isNextStatus(order.getStatus(), request.getStatus()))
            throw new BadRequestException("Invalid status transition from " + order.getStatus() + " to " + request.getStatus());

        order.setStatus(request.getStatus());
        return mapToResponse(orderRepo.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        verifyOrderReadAccess(order);

        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.PREPARING)
            throw new OrderCancellationException("Only PLACED or PREPARING orders can be cancelled. Current status: " + order.getStatus());

        WalletRequest refundRequest = WalletRequest.builder()
                .userId(order.getUserId())
                .amount(order.getTotalAmount())
                .build();

        try {
            walletRestClient.put()
                    .uri("/wallet/addMoney")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(refundRequest)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            log.error("Wallet refund failed for order {}: {}", order.getOrderId(), ex.getResponseBodyAsString());
            throw new PaymentException("Refund failed: " + ex.getResponseBodyAsString());
        }

        order.setStatus(OrderStatus.CANCELLED);
        log.info("Order {} cancelled and refund requested", order.getOrderId());
        return mapToResponse(orderRepo.save(order));
    }

    private void verifyOrderReadAccess(Order order) {
        User loggedInUser = currentUserService.getCurrentUser();
        if (loggedInUser.getRole().getRoleName() == RoleType.ADMIN || loggedInUser.getUserId().equals(order.getUserId())) {
            return;
        }
        managerAccessService.verifyCanteenAccess(order.getCanteenId());
    }

    private Order saveWithRetry(Order order) {
        RuntimeException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return orderRepo.save(order);
            } catch (RuntimeException ex) {
                lastException = ex;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw ex;
                }
            }
        }
        throw lastException;
    }

    private boolean isNextStatus(OrderStatus current, OrderStatus next) {
        return (current == OrderStatus.PLACED && next == OrderStatus.PREPARING)
                || (current == OrderStatus.PREPARING && next == OrderStatus.READY)
                || (current == OrderStatus.READY && next == OrderStatus.COMPLETED);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .canteenId(order.getCanteenId())
                .foodId(order.getFoodId())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .pickupSlot(order.getPickUpSlot())
                .createdAt(order.getCreatedAt())   // ← was missing
                .build();
    }
}
