import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginManager } from "../../services/authService";
import { useAuth } from "../../context/AuthContext";

export default function ManagerLogin() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError("");
    setMessage("");
    try {
      const response = await loginManager(formData);
      const data = response.data || {};
      if (data.role !== "CANTEEN_MANAGER") {
        setError("This account is not registered as a canteen manager.");
        return;
      }
      login(data, data.token || "");
      setMessage("Login successful.");
      navigate("/manager/dashboard");
    } catch (err) {
      setError(err?.response?.data?.message || "Invalid manager credentials.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page d-flex align-items-center justify-content-center min-vh-100">
      <div className="card shadow-sm border-0 rounded-4 auth-card">
        <div className="card-body p-4 p-md-5">
          <h2 className="fw-bold text-success mb-2">Manager Login</h2>
          <p className="text-muted mb-4">Access the canteen manager panel.</p>

          {message ? <div className="alert alert-success py-2">{message}</div> : null}
          {error ? <div className="alert alert-danger py-2">{error}</div> : null}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Email / Username</label>
              <input
                type="email"
                className="form-control"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-4">
              <label className="form-label">Password</label>
              <input
                type="password"
                className="form-control"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>
            <button className="btn btn-success w-100" disabled={loading}>
              {loading ? "Logging in..." : "Login"}
            </button>
          </form>
          <p className="small text-center mt-3 mb-0">
            New manager? <Link to="/register">Register here</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
