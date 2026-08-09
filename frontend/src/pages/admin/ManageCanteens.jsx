import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import apiClient from "../../services/apiClient";

export default function ManageCanteens() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [canteens, setCanteens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [form, setForm] = useState({ canteenName: "", openingTime: "", closingTime: "" });
  const [submitting, setSubmitting] = useState(false);

  const handleLogout = () => { logout(); navigate("/login"); };

  const load = async () => {
    try {
      setLoading(true);
      const res = await apiClient.get("/admin/canteens");
      setCanteens(res.data);
    } catch (e) {
      setError("Failed to load canteens");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      setSubmitting(true);
      await apiClient.post("/admin/canteens", form);
      setSuccess("Canteen added successfully!");
      setForm({ canteenName: "", openingTime: "", closingTime: "" });
      load();
    } catch (e) {
      setError(e?.response?.data?.message || "Failed to add canteen");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this canteen?")) return;
    try {
      await apiClient.delete(`/admin/canteens/${id}`);
      setSuccess("Canteen deleted!");
      load();
    } catch (e) {
      setError(e?.response?.data?.message || "Failed to delete canteen");
    }
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
        <h1 className="h3 fw-bold mb-4">Manage Canteens</h1>

        {error && <div className="alert alert-danger">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        {/* Add Canteen Form */}
        <div className="card border-0 shadow-sm rounded-4 mb-4">
          <div className="card-body p-4">
            <h5 className="fw-bold mb-3">Add New Canteen</h5>
            <form onSubmit={handleAdd} className="row g-3">
              <div className="col-md-4">
                <input className="form-control" placeholder="Canteen Name" required
                  value={form.canteenName} onChange={e => setForm({...form, canteenName: e.target.value})} />
              </div>
              <div className="col-md-3">
                <input className="form-control" type="time" placeholder="Opening Time"
                  value={form.openingTime} onChange={e => setForm({...form, openingTime: e.target.value})} />
              </div>
              <div className="col-md-3">
                <input className="form-control" type="time" placeholder="Closing Time"
                  value={form.closingTime} onChange={e => setForm({...form, closingTime: e.target.value})} />
              </div>
              <div className="col-md-2">
                <button className="btn btn-success w-100" type="submit" disabled={submitting}>
                  {submitting ? "Adding..." : "Add Canteen"}
                </button>
              </div>
            </form>
          </div>
        </div>

        {/* Canteens Table */}
        <div className="card border-0 shadow-sm rounded-4">
          <div className="card-body">
            {loading ? (
              <div className="text-center py-4"><div className="spinner-border text-success" /></div>
            ) : (
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>ID</th><th>Name</th><th>Opens</th><th>Closes</th><th>Status</th><th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {canteens.map(c => (
                      <tr key={c.canteenId}>
                        <td>{c.canteenId}</td>
                        <td className="fw-semibold">{c.canteenName}</td>
                        <td>{c.openingTime || "-"}</td>
                        <td>{c.closingTime || "-"}</td>
                        <td><span className={`badge ${c.active ? "bg-success" : "bg-secondary"}`}>{c.active ? "Active" : "Inactive"}</span></td>
                        <td>
                          <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(c.canteenId)}>Delete</button>
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
