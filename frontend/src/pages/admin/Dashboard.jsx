import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import apiClient from "../../services/apiClient";

export default function AdminDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [stats, setStats] = useState({ users: 0, canteens: 0 });
  const [loading, setLoading] = useState(true);

  const handleLogout = () => { logout(); navigate("/login"); };

  useEffect(() => {
    const load = async () => {
      try {
        const [usersRes, canteensRes] = await Promise.all([
          apiClient.get("/admin/users"),
          apiClient.get("/admin/canteens"),
        ]);
        setStats({ users: usersRes.data.length, canteens: canteensRes.data.length });
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  return (
    <div className="min-vh-100 bg-light">
      {/* Navbar */}
      <nav className="navbar navbar-dark bg-dark px-4">
        <span className="navbar-brand fw-bold">🍽️ SmartBite Admin</span>
        <div className="d-flex align-items-center gap-3">
          <span className="text-light small">{user?.name}</span>
          <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Logout</button>
        </div>
      </nav>

      <div className="container-fluid p-4">
        <div className="row g-3 mb-4">
          <div className="col-12">
            <h1 className="h3 fw-bold">Admin Dashboard</h1>
            <p className="text-muted">System overview and management</p>
          </div>
        </div>

        {/* Stats */}
        <div className="row g-3 mb-4">
          <div className="col-md-3">
            <div className="card border-0 shadow-sm rounded-4">
              <div className="card-body">
                <div className="text-muted small">Total Users</div>
                <div className="h2 fw-bold text-primary">{loading ? "..." : stats.users}</div>
              </div>
            </div>
          </div>
          <div className="col-md-3">
            <div className="card border-0 shadow-sm rounded-4">
              <div className="card-body">
                <div className="text-muted small">Total Canteens</div>
                <div className="h2 fw-bold text-success">{loading ? "..." : stats.canteens}</div>
              </div>
            </div>
          </div>
        </div>

        {/* Quick Links */}
        <div className="row g-3">
          <div className="col-md-4">
            <Link to="/admin/canteens" className="card border-0 shadow-sm rounded-4 text-decoration-none">
              <div className="card-body p-4">
                <div className="fs-1">🏪</div>
                <h5 className="fw-bold mt-2">Manage Canteens</h5>
                <p className="text-muted small">Add, edit, or remove canteens</p>
              </div>
            </Link>
          </div>
          <div className="col-md-4">
            <Link to="/admin/users" className="card border-0 shadow-sm rounded-4 text-decoration-none">
              <div className="card-body p-4">
                <div className="fs-1">👥</div>
                <h5 className="fw-bold mt-2">Manage Users</h5>
                <p className="text-muted small">View all users and assign roles</p>
              </div>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
