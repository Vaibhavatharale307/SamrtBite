import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidebar from "../../components/manager/Sidebar";
import Navbar from "../../components/manager/Navbar";
import StatCard from "../../components/manager/StatCard";
import { getOrdersByCanteen } from "../../services/orderService";
import { getMenuByCanteen } from "../../services/canteenService";
import { getLoggedInUser, logoutUser } from "../../services/authService";

const getStatusClass = (status) => {
  const value = String(status || "").toUpperCase();
  if (value === "PLACED") return "bg-primary";
  if (value === "PREPARING") return "bg-warning text-dark";
  if (value === "READY") return "bg-purple";
  if (value === "COMPLETED") return "bg-success";
  if (value === "CANCELLED") return "bg-danger";
  return "bg-secondary";
};

export default function Dashboard() {
  const navigate = useNavigate();
  const user = getLoggedInUser();
  const canteenId = user?.canteenId || user?.canteen?.canteenId || user?.canteen?.id;
  const [orders, setOrders] = useState([]);
  const [menuItems, setMenuItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadData = async () => {
      if (!canteenId) {
        setError("Canteen details are missing from the logged-in account.");
        setLoading(false);
        return;
      }
      try {
        const [ordersRes, menuRes] = await Promise.all([
          getOrdersByCanteen(canteenId),
          getMenuByCanteen(canteenId),
        ]);
        setOrders(ordersRes.data || []);
        setMenuItems(menuRes.data || []);
      } catch (err) {
        setError(err?.response?.data?.message || "Failed to load dashboard.");
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, [canteenId]);

  const todayOrders = orders.length;
  const pendingOrders = orders.filter((order) => String(order.status).toUpperCase() !== "COMPLETED").length;
  const todayRevenue = orders.reduce((sum, order) => sum + Number(order.amount || order.totalAmount || 0), 0);

  const handleLogout = () => {
    logoutUser();
    navigate("/manager/login");
  };

  return (
    <div className="manager-shell d-flex">
      <Sidebar onLogout={handleLogout} />
      <main className="flex-grow-1 p-3 p-md-4">
        <Navbar
          title="Dashboard"
          subtitle="Simple overview of today’s kitchen activity"
          onLogout={handleLogout}
        />

        <div className="row g-3 mt-1">
          <div className="col-md-6 col-xl-3">
            <StatCard title="Today's Orders" value={todayOrders} />
          </div>
          <div className="col-md-6 col-xl-3">
            <StatCard title="Today's Revenue" value={`₹${todayRevenue.toFixed(2)}`} />
          </div>
          <div className="col-md-6 col-xl-3">
            <StatCard title="Pending Orders" value={pendingOrders} />
          </div>
          <div className="col-md-6 col-xl-3">
            <StatCard title="Menu Items" value={menuItems.length} />
          </div>
        </div>

        {error ? <div className="alert alert-danger mt-3">{error}</div> : null}
        {loading ? (
          <div className="text-center py-5">
            <div className="spinner-border text-success" role="status" />
          </div>
        ) : (
          <div className="row g-3 mt-1">
            <div className="col-lg-8">
              <div className="card rounded-4 shadow-sm border-0 mt-3">
                <div className="card-body">
                  <h5 className="mb-3">Recent Orders</h5>
                  <div className="table-responsive">
                    <table className="table align-middle">
                      <thead>
                        <tr>
                          <th>Order ID</th>
                          <th>Food Name</th>
                          <th>Quantity</th>
                          <th>Amount</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {orders.slice(0, 6).map((order) => (
                          <tr key={order.orderId}>
                            <td>{order.orderId}</td>
                            <td>{order.foodName || order.menuItem || "-"}</td>
                            <td>{order.quantity}</td>
                            <td>₹{Number(order.amount || order.totalAmount || 0).toFixed(2)}</td>
                            <td>
                              <span className={`badge ${getStatusClass(order.status)}`}>
                                {order.status}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
            <div className="col-lg-4">
              <div className="card rounded-4 shadow-sm border-0 mt-3">
                <div className="card-body">
                  <h5 className="mb-3">Recent Activity</h5>
                  <ul className="list-group list-group-flush">
                    <li className="list-group-item px-0">Orders are updated from the Orders page.</li>
                    <li className="list-group-item px-0">Menu items can be managed from Manage Menu.</li>
                    <li className="list-group-item px-0">All data is pulled directly from the backend.</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
