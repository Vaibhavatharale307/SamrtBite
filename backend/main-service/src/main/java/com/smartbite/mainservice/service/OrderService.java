package com.smartbite.mainservice.service;

import java.util.List;
import com.smartbite.mainservice.dto.*;

public interface OrderService {
	OrderResponse placeOrder(OrderRequest request);

	OrderResponse getOrderById(Long id);

	List<OrderResponse> getOrderByUser(Long id);

	List<OrderResponse> getOrdersByCanteen(Long id);

	OrderResponse updateOrderStatus(Long id, OrderStatusRequest request);

	OrderResponse cancelOrder(Long id);
}
