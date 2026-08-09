package com.smartbite.mainservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.smartbite.mainservice.dto.MenuResponse;
import com.smartbite.mainservice.dto.OrderRequest;
import com.smartbite.mainservice.entity.Order;
import com.smartbite.mainservice.entity.OrderStatus;
import com.smartbite.mainservice.entity.Role;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.exception.FoodUnavailableException;
import com.smartbite.mainservice.repository.OrderRepository;
import com.smartbite.mainservice.security.CurrentUserService;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CanteenService canteenService;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private ManagerAccessService managerAccessService;

    private OrderServiceImpl orderService;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RestClient.Builder builder = RestClient.builder().baseUrl("http://wallet-test");
        server = MockRestServiceServer.bindTo(builder).build();
        orderService = new OrderServiceImpl(orderRepository, canteenService, builder.build(), currentUserService, managerAccessService);
    }

    @Test
    void placeOrderDeductsWalletAndSavesPlacedOrder() {
        when(currentUserService.getCurrentUser()).thenReturn(user(1L, RoleType.STUDENT));
        when(canteenService.getMenuByFoodId(10L)).thenReturn(menu(true));
        when(orderRepository.countByPickUpSlot("10:00")).thenReturn(0L);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getOrderId() == null) {
                order.setOrderId(99L);
            }
            return order;
        });

        server.expect(requestTo("http://wallet-test/wallet/deductMoney"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess());

        OrderRequest request = OrderRequest.builder()
                .userId(1L)
                .canteenId(5L)
                .foodId(10L)
                .quantity(2)
                .pickupSlot("10:00")
                .build();

        assertEquals(OrderStatus.PLACED, orderService.placeOrder(request).getStatus());
        server.verify();
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void placeOrderWithUnavailableFoodThrows() {
        when(currentUserService.getCurrentUser()).thenReturn(user(1L, RoleType.STUDENT));
        when(canteenService.getMenuByFoodId(10L)).thenReturn(menu(false));

        OrderRequest request = OrderRequest.builder()
                .userId(1L)
                .canteenId(5L)
                .foodId(10L)
                .quantity(1)
                .pickupSlot("10:00")
                .build();

        assertThrows(FoodUnavailableException.class, () -> orderService.placeOrder(request));
    }

    @Test
    void cancelOrderRefundsWalletAndMarksCancelled() {
        when(currentUserService.getCurrentUser()).thenReturn(user(1L, RoleType.STUDENT));
        when(orderRepository.findById(99L)).thenReturn(Optional.of(order(OrderStatus.PLACED)));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        server.expect(requestTo("http://wallet-test/wallet/addMoney"))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess());

        assertEquals(OrderStatus.CANCELLED, orderService.cancelOrder(99L).getStatus());
        server.verify();
    }

    @Test
    void getOrderByIdRejectsWrongStudent() {
        when(currentUserService.getCurrentUser()).thenReturn(user(2L, RoleType.STUDENT));
        when(orderRepository.findById(99L)).thenReturn(Optional.of(order(OrderStatus.PLACED)));
        doThrow(new AccessDeniedException("Denied")).when(managerAccessService).verifyCanteenAccess(5L);

        assertThrows(AccessDeniedException.class, () -> orderService.getOrderById(99L));
    }

    private MenuResponse menu(boolean available) {
        return MenuResponse.builder()
                .foodId(10L)
                .canteenId(5L)
                .foodName("Poha")
                .price(new BigDecimal("25.00"))
                .available(available)
                .build();
    }

    private Order order(OrderStatus status) {
        return Order.builder()
                .orderId(99L)
                .userId(1L)
                .canteenId(5L)
                .foodId(10L)
                .quantity(1)
                .totalAmount(new BigDecimal("25.00"))
                .status(status)
                .pickUpSlot("10:00")
                .build();
    }

    private User user(Long userId, RoleType roleType) {
        Role role = new Role();
        role.setRoleName(roleType);
        User user = new User();
        user.setUserId(userId);
        user.setRole(role);
        return user;
    }
}
