import { useEffect, useState } from "react";
import Sidebar from "../../components/manager/Sidebar";
import Navbar from "../../components/manager/Navbar";
import { getLoggedInUser, logoutUser } from "../../services/authService";
import { getOrdersByCanteen, updateOrderStatus } from "../../services/orderService";
import { useNavigate } from "react-router-dom";

const statusFlow = {
  PLACED: "PREPARING",
  PREPARING: "READY",
  READY: "COMPLETED",
};

const statusClass = (status) => {
  const value = String(status || "").toUpperCase();
  if (value === "PLACED") return "bg-primary";
  if (value === "PREPARING") return "bg-warning text-dark";
  if (value === "READY") return "bg-purple";
  if (value === "COMPLETED") return "bg-success";
  if (value === "CANCELLED") return "bg-danger";
  return "bg-secondary";
};

export default function Orders() {
  const navigate = useNavigate();
  const user = getLoggedInUser();
  const canteenId = user?.canteenId || user?.canteen?.canteenId || user?.canteen?.id;
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const loadOrders = async () => {
    try {
      const response = await getOrdersByCanteen(canteenId);
      setOrders(response.data || []);
    } catch (err) {
      setError(err?.response?.data?.message || "Unable to load orders.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!canteenId) {
      setError("Canteen details are missing from the logged-in account.");
      setLoading(false);
      return;
    }
    loadOrders();
  }, [canteenId]);

  const handleLogout = () => {
    logoutUser();
    navigate("/manager/login");
  };

  const handleStatusChange = async (orderId, currentStatus) => {
    const nextStatus = statusFlow[String(currentStatus || "").toUpperCase()];
    if (!nextStatus) {
      setError("This order cannot be updated further.");
      return;
    }
    try {
      setMessage("");
      setError("");
      await updateOrderStatus(orderId, nextStatus);
      setMessage(`Order ${orderId} moved to ${nextStatus}.`);
      await loadOrders();
    } catch (err) {
      setError(err?.response?.data?.message || "Failed to update order status.");
    }
  };

  return (
    <div className="manager-shell d-flex">
      <Sidebar onLogout={handleLogout} />
      <main className="flex-grow-1 p-3 p-md-4">
        <Navbar title="Orders" subtitle="Track and update canteen orders" onLogout={handleLogout} />

        {message ? <div className="alert alert-success mt-3">{message}</div> : null}
        {error ? <div className="alert alert-danger mt-3">{error}</div> : null}

        <div className="card rounded-4 shadow-sm border-0 mt-3">
          <div className="card-body">
            {loading ? (
              <div className="text-center py-5">
                <div className="spinner-border text-success" role="status" />
              </div>
            ) : (
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Student Name</th>
                      <th>Food Name</th>
                      <th>Quantity</th>
                      <th>Pickup Slot</th>
                      <th>Amount</th>
                      <th>Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.map((order) => (
                      <tr key={order.orderId}>
                        <td>{order.orderId}</td>
                        <td>{order.studentName || order.userName || "-"}</td>
                        <td>{order.foodName || order.menuItem || "-"}</td>
                        <td>{order.quantity}</td>
                        <td>{order.pickupSlot || "-"}</td>
                        <td>₹{Number(order.amount || order.totalAmount || 0).toFixed(2)}</td>
                        <td>
                          <span className={`badge ${statusClass(order.status)}`}>{order.status}</span>
                        </td>
                        <td>
                          <button
                            className="btn btn-sm btn-success"
                            onClick={() => handleStatusChange(order.orderId, order.status)}
                            disabled={!statusFlow[String(order.status || "").toUpperCase()]}
                          >
                            Update Status
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
