import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import apiClient from "../../services/apiClient";

export default function ManageUsers() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleLogout = () => { logout(); navigate("/login"); };

  const load = async () => {
    try {
      setLoading(true);
      const res = await apiClient.get("/admin/users");
      setUsers(res.data);
    } catch (e) {
      setError("Failed to load users");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this user?")) return;
    try {
      await apiClient.delete(`/admin/users/${id}`);
      setSuccess("User deleted!");
      load();
    } catch (e) {
      setError(e?.response?.data?.message || "Failed to delete user");
    }
  };

  const roleBadge = (role) => {
    const map = { ADMIN: "bg-danger", CANTEEN_MANAGER: "bg-warning text-dark", STUDENT: "bg-primary" };
    return map[role] || "bg-secondary";
  };

  return (
    <div className="min-vh-100 bg-light">
      <nav className="navbar navbar-dark bg-dark px-4">
        <span className="navbar-brand fw-bold">🍽️ SmartBite Admin</span>
        <div className="d-flex align-items-center gap-3">
          <Link to="/admin/dashboard" className="btn btn-outline-light btn-sm">Dashboard</Link>
          <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Logout</button>
        </div>
      </nav>

      <div className="container py-4">
        <h1 className="h3 fw-bold mb-4">Manage Users</h1>

        {error && <div className="alert alert-danger">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        <div className="card border-0 shadow-sm rounded-4">
          <div className="card-body">
            {loading ? (
              <div className="text-center py-4"><div className="spinner-border text-success" /></div>
            ) : (
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th><th>Canteen</th><th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map(u => (
                      <tr key={u.userId}>
                        <td>{u.userId}</td>
                        <td className="fw-semibold">{u.name}</td>
                        <td>{u.email}</td>
                        <td>{u.phone || "-"}</td>
                        <td><span className={`badge ${roleBadge(u.role?.roleName)}`}>{u.role?.roleName}</span></td>
                        <td>{u.canteen?.canteenName || "-"}</td>
                        <td>
                          <button className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDelete(u.userId)}
                            disabled={u.role?.roleName === "ADMIN"}
                          >Delete</button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
