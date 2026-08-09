import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { loginUser } from "../../services/authService";
import { useAuth } from "../../context/AuthContext";

/*
Student Login — route: /student-login
Uses the same /auth/login API as Login.jsx.
After login, validates role === "STUDENT" before navigating.
*/

export default function StudentLogin() {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await loginUser(form);

      const userData = response.data;
      const token = userData.token || userData.jwt || "";

      /* Role guard: if manager/admin tried the student login, reject */

      if (userData.role === "CANTEEN_MANAGER") {
        setError(
          "Manager access detected. Please use 'Login as Canteen Manager' from the home page.",
        );
        return;
      }

      if (userData.role === "ADMIN") {
        setError("Admin account detected. Please contact your administrator.");
        return;
      }

      login(userData, token);
      navigate("/dashboard");
    } catch (err) {
      const msg = err?.response?.data?.message;

      if (err?.response?.status === 401 || err?.response?.status === 403) {
        setError("Invalid email or password.");
      } else if (!err?.response) {
        setError("Unable to connect to SmartBite server. Please try again.");
      } else {
        setError(msg || "Unable to login. Check your credentials.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page">
      <section className="auth-card">
        {/* Brand */}
        <Link to="/" style={{ textDecoration: "none" }}>
          <span className="brand-mark">S</span>
        </Link>

        <h1 className="h3 mt-3">Student Login</h1>

        <p className="text-muted">Access your campus dining account.</p>

        {/* Error */}
        {error && (
          <div className="alert alert-danger text-start" role="alert">
            {error}
          </div>
        )}

        {/* Login Form */}
        <form className="text-start" onSubmit={handleSubmit} noValidate>
          {/* Email */}
          <div className="mb-3">
            <label className="form-label" htmlFor="sl-email">
              Email
            </label>

            <input
              id="sl-email"
              type="email"
              className="form-control"
              placeholder="Enter your email"
              required
              autoComplete="email"
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
          <div className="mb-1">
            <label className="form-label" htmlFor="sl-password">
              Password
            </label>

            <input
              id="sl-password"
              type="password"
              className="form-control"
              placeholder="Enter your password"
              required
              autoComplete="current-password"
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
            aria-label="Login as student"
          >
            {loading ? "Logging in…" : "Login"}
          </button>
        </form>

        <hr className="my-3" />

        {/* Registration */}
        <p className="small mb-1 text-muted">New to SmartBite?</p>

        <Link
          to="/register"
          className="btn btn-outline-success w-100 btn-sm"
          aria-label="Create a student account"
        >
          Create Student Account
        </Link>

        {/* Manager Login */}
        <p className="small mt-3 mb-0 text-muted">
          Are you a manager? <Link to="/manager-login">Manager Login →</Link>
        </p>
      </section>
    </main>
  );
}
