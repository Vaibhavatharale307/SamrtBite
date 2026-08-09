import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { loginUser } from "../../services/authService";
import { useAuth } from "../../context/AuthContext";

/*
  Canteen Manager Login — route: /manager-login
  Uses the same /auth/login API.
  After login, validates role === "CANTEEN_MANAGER" before navigating.
  No registration link shown (managers are created by admin/backend).
*/
export default function ManagerLoginPage() {
  const [form, setForm]       = useState({ email: "", password: "" });
  const [error, setError]     = useState("");
  const [loading, setLoading] = useState(false);
  const { login }             = useAuth();
  const navigate              = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const response = await loginUser(form);
      const userData = response.data;
      const token    = userData.token || userData.jwt || "";

      /* Role guard: only CANTEEN_MANAGER is allowed here */
      if (userData.role === "STUDENT") {
        setError("Student access detected. Please use 'Login as Student' from the home page.");
        return;
      }
      if (userData.role !== "CANTEEN_MANAGER") {
        setError("Manager access required. This portal is for canteen managers only.");
        return;
      }

      login(userData, token);
      navigate("/manager/dashboard");
    } catch (err) {
      if (err?.response?.status === 401 || err?.response?.status === 403) {
        setError("Invalid email or password.");
      } else if (!err?.response) {
        setError("Unable to connect to SmartBite server. Please try again.");
      } else {
        setError(err?.response?.data?.message || "Invalid manager credentials.");
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
        <h1 className="h3 mt-3">Canteen Manager Login</h1>
        <p className="text-muted">Access the canteen management portal.</p>

        {/* Manager-specific visual hint */}
        <div
          style={{
            background: "#f0fdf4",
            border: "1px solid #bbf7d0",
            borderRadius: 10,
            padding: ".6rem .85rem",
            fontSize: ".82rem",
            color: "#166534",
            marginBottom: "1rem",
            textAlign: "left",
          }}
        >
          🏪 Manager accounts are provisioned by SmartBite administration.
        </div>

        {error && (
          <div className="alert alert-danger text-start" role="alert">
            {error}
          </div>
        )}

        <form className="text-start" onSubmit={handleSubmit} noValidate>
          <div className="mb-3">
            <label className="form-label" htmlFor="ml-email">Email</label>
            <input
              id="ml-email"
              type="email"
              className="form-control"
              required
              autoComplete="email"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
            />
          </div>
          <div className="mb-3">
            <label className="form-label" htmlFor="ml-password">Password</label>
            <input
              id="ml-password"
              type="password"
              className="form-control"
              required
              autoComplete="current-password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
            />
          </div>
          <button
            type="submit"
            className="btn btn-success w-100"
            disabled={loading}
            aria-label="Login as canteen manager"
          >
            {loading ? "Logging in…" : "Login"}
          </button>
        </form>

        <p className="small mt-3 mb-0 text-muted">
          Are you a student?{" "}
          <Link to="/student-login">Student Login →</Link>
        </p>
      </section>
    </main>
  );
}
