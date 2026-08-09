import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import OrderCard from "../components/OrderCard";
import { cancelOrder, getOrdersByUser } from "../services/orderService";
import { getMenuItem } from "../services/canteenService";
import { useAuth } from "../context/AuthContext";
export default function MyOrders() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");
  const loadOrders = async () => {
    try {
      const response = await getOrdersByUser(user.userId);
      const detailedOrders = await Promise.all(
        response.data.map(async (order) => {
          try {
            const menu = await getMenuItem(order.foodId);
            return { ...order, foodName: menu.data.foodName };
          } catch {
            return order;
          }
        }),
      );
      setOrders(detailedOrders);
    } catch {
      setError("Unable to load your orders.");
    }
  };
  useEffect(() => {
    loadOrders();
  }, [user.userId]);
  const handleCancel = async (orderId) => {
    try {
      await cancelOrder(orderId);
      loadOrders();
    } catch (apiError) {
      setError(
        apiError.response?.data?.message || "Unable to cancel this order.",
      );
    }
  };
  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <h1 className="h3">My Orders</h1>
        <p className="text-muted">Track and manage your food orders.</p>
        {error && <div className="alert alert-danger">{error}</div>}
        {orders.map((order) => (
          <OrderCard
            key={order.orderId}
            order={order}
            onCancel={handleCancel}
          />
        ))}
        {!orders.length && !error && (
          <p className="text-muted">You have not placed an order yet.</p>
        )}
      </main>
    </div>
  );
}
