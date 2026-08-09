import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { registerUser } from "../services/authService";
import { getAllCanteens } from "../services/canteenService";

export default function Register() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    role: "STUDENT",
    canteenId: "",
  });
  const [canteens, setCanteens] = useState([]);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const updateForm = (event) =>
    setForm({ ...form, [event.target.name]: event.target.value });
  const [canteenError, setCanteenError] = useState(false);
  useEffect(() => {
    getAllCanteens()
      .then((response) => setCanteens(response.data || []))
      .catch(() => setCanteenError(true));
  }, []);
  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setMessage("");
    setLoading(true);
    try {
      const response = await registerUser(form);
      setMessage(
        response.data.message || "Registration successful. You can login now.",
      );
      setTimeout(() => navigate(form.role === "CANTEEN_MANAGER" ? "/manager/login" : "/login"), 1200);
    } catch (apiError) {
      setError(apiError.response?.data?.message || "Unable to register.");
    } finally {
      setLoading(false);
    }
  };
  return (
    <main className="auth-page">
      <section className="auth-card">
        <span className="brand-mark">S</span>
        <h1 className="h3 mt-3">Create SmartBite Account</h1>
        <p className="text-muted">Create your campus dining account.</p>
        {error && <div className="alert alert-danger text-start">{error}</div>}
        {message && (
          <div className="alert alert-success text-start">{message}</div>
        )}
        <form className="text-start" onSubmit={handleSubmit}>
          <div className="mb-2">
            <label className="form-label">Name</label>
            <input
              name="name"
              className="form-control"
              required
              value={form.name}
              onChange={updateForm}
            />
          </div>
          <div className="mb-2">
            <label className="form-label">Email</label>
            <input
              name="email"
              type="email"
              className="form-control"
              required
              value={form.email}
              onChange={updateForm}
            />
          </div>
          <div className="mb-2">
            <label className="form-label">Password</label>
            <input
              name="password"
              type="password"
              className="form-control"
              minLength="6"
              required
              value={form.password}
              onChange={updateForm}
            />
          </div>
          <div className="mb-2">
            <label className="form-label">Phone</label>
            <input
              name="phone"
              className="form-control"
              pattern="[6-9][0-9]{9}"
              required
              value={form.phone}
              onChange={updateForm}
            />
          </div>
          {/* Role is always STUDENT for public registration.
              Canteen Manager accounts are created by admin/backend only. */}
          <input type="hidden" name="role" value="STUDENT" />
          <button className="btn btn-success w-100" disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </button>
        </form>
        <p className="small mt-3 mb-0">
          Already registered?{" "}
          <Link to="/student-login">Login here</Link>
        </p>
      </section>
    </main>
  );
}
