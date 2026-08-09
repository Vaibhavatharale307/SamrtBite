import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { loginUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await loginUser(form);

      const userData = response.data;
      const token = response.data.token || response.data.jwt;

      login(userData, token);

      if (userData.role === "ADMIN") {
        navigate("/admin/dashboard");
      } else if (userData.role === "CANTEEN_MANAGER") {
        navigate("/manager/dashboard");
      } else {
        navigate("/dashboard");
      }
    } catch (apiError) {
      setError(
        apiError.response?.data?.message ||
          "Unable to login. Check your credentials.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page">
      <section className="auth-card">
        {/* Brand */}
        <span className="brand-mark">S</span>

        <h1 className="h3 mt-3">Welcome to SmartBite</h1>

        <p className="text-muted">Campus dining made simple.</p>

        {/* Error Message */}
        {error && <div className="alert alert-danger text-start">{error}</div>}

        {/* Login Form */}
        <form className="text-start" onSubmit={handleSubmit}>
          {/* Email */}
          <div className="mb-3">
            <label className="form-label">Email</label>

            <input
              type="email"
              className="form-control"
              placeholder="Enter your email"
              required
              value={form.email}
              onChange={(e) =>
                setForm({
                  ...form,
                  email: e.target.value,
                })
              }
            />
          </div>

          {/* Password */}
          <div className="mb-2">
            <label className="form-label">Password</label>

            <input
              type="password"
              className="form-control"
              placeholder="Enter your password"
              required
              value={form.password}
              onChange={(e) =>
                setForm({
                  ...form,
                  password: e.target.value,
                })
              }
            />
          </div>

          {/* Forgot Password */}
          <div className="text-end mb-3">
            <Link to="/forgot-password" className="small text-decoration-none">
              Forgot Password?
            </Link>
          </div>

          {/* Login Button */}
          <button
            type="submit"
            className="btn btn-success w-100"
            disabled={loading}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        {/* Registration */}
        <div className="text-center small mt-3">
          New student? <Link to="/register">Register here</Link>
        </div>
      </section>
    </main>
  );
}
