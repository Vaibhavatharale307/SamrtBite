import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import DashboardCard from "../components/DashboardCard";
import { useEffect, useState } from "react";
import { getAllCanteens } from "../services/canteenService";
import { getOrdersByUser } from "../services/orderService";
import { getWallet } from "../services/walletService";
import { useAuth } from "../context/AuthContext";

export default function Dashboard() {
  const { user } = useAuth();
  const [wallet, setWallet] = useState(0);
  const [orders, setOrders] = useState([]);
  const [canteens, setCanteens] = useState([]);
  useEffect(() => {
    const load = async () => {
      try {
        const [walletRes, ordersRes, canteensRes] = await Promise.all([
          getWallet(user.userId),
          getOrdersByUser(user.userId),
          getAllCanteens(),
        ]);
        setWallet(walletRes.data.balance);
        setOrders(ordersRes.data);
        setCanteens(canteensRes.data);
      } catch {
        /* Individual pages display detailed API errors. */
      }
    };
    load();
  }, [user.userId]);
  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <div className="mb-4">
          <h1 className="h3 mb-1">Dashboard</h1>
          <p className="text-muted mb-0">
            A quick view of your campus dining activity.
          </p>
        </div>

        <section className="row g-3 mb-4">
          <div className="col-md-6 col-xl-4">
            <DashboardCard
              title="Wallet Balance"
              value={`₹${Number(wallet).toFixed(2)}`}
              note="Available balance"
            />
          </div>
          <div className="col-md-6 col-xl-4">
            <DashboardCard
              title="Total Orders"
              value={orders.length}
              note="Your order history"
            />
          </div>
          <div className="col-md-6 col-xl-4">
            <DashboardCard
              title="Active Canteens"
              value={canteens.filter((canteen) => canteen.active).length}
              note="Available on campus"
            />
          </div>
        </section>

        <section className="row g-4">
          <div className="col-lg-7">
            <div className="content-card">
              <h2 className="h5 mb-3">Recent Orders</h2>
              <div className="table-responsive">
                <table className="table align-middle mb-0">
                  <thead>
                    <tr>
                      <th>Order</th>
                      <th>Food</th>
                      <th>Status</th>
                      <th>Amount</th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.slice(0, 5).map((order) => (
                      <tr key={order.orderId}>
                        <td>#{order.orderId}</td>
                        <td>Food #{order.foodId}</td>
                        <td>
                          <span className="status-badge">{order.status}</span>
                        </td>
                        <td>₹{order.totalAmount}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              {!orders.length && (
                <p className="small text-muted mt-3 mb-0">No orders found.</p>
              )}
            </div>
          </div>
          <div className="col-lg-5">
            <div className="content-card">
              <h2 className="h5 mb-3">Popular Canteens</h2>
              <ul className="list-group list-group-flush">
                {canteens.slice(0, 5).map((canteen) => (
                  <li key={canteen.canteenId} className="list-group-item px-0">
                    {canteen.canteenName}
                  </li>
                ))}
              </ul>
              {!canteens.length && (
                <p className="small text-muted mt-3 mb-0">No canteens found.</p>
              )}
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}
