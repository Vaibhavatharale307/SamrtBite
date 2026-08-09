import apiClient from "./apiClient";

export const placeOrder = (orderData) => apiClient.post("/order", orderData);
export const getOrdersByUser = (userId) => apiClient.get(`/order/user/${userId}`);
export const cancelOrder = (orderId) => apiClient.put(`/order/cancel/${orderId}`);

export const getOrdersByCanteen = (canteenId) =>
  apiClient.get(`/order/canteen/${canteenId}`);

export const updateOrderStatus = (orderId, status) =>
  apiClient.put(`/order/${orderId}/status`, { status });
