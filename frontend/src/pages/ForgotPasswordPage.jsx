import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { forgotPassword } from "../services/authService";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setMessage("");

    if (!email.trim()) {
      setError("Please enter your email address.");
      return;
    }

    try {
      setLoading(true);

      await forgotPassword(email);

      sessionStorage.setItem("resetEmail", email);

      setMessage("OTP has been sent to your email.");

      setTimeout(() => {
        navigate("/verify-otp");
      }, 1000);
    } catch (err) {
      console.error(err);

      setError(
        err?.response?.data?.message || "Unable to send OTP. Please try again.",
      );
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

        {/* Heading */}
        <h1 className="h3 mt-3">Forgot Password?</h1>

        <p className="text-muted">
          Enter your registered email address and we'll send you an OTP to reset
          your password.
        </p>

        {/* Error */}
        {error && (
          <div className="alert alert-danger text-start" role="alert">
            {error}
          </div>
        )}

        {/* Success */}
        {message && (
          <div className="alert alert-success text-start" role="alert">
            {message}
          </div>
        )}

        {/* Form */}
        <form className="text-start" onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label" htmlFor="forgot-email">
              Email Address
            </label>

            <input
              id="forgot-email"
              type="email"
              className="form-control"
              placeholder="Enter your registered email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="btn btn-success w-100"
            disabled={loading}
          >
            {loading ? "Sending OTP..." : "Send OTP"}
          </button>
        </form>

        <hr className="my-3" />

        {/* Back to Login */}
        <Link
          to="/student-login"
          className="btn btn-outline-success w-100 btn-sm"
        >
          ← Back to Student Login
        </Link>
      </section>
    </main>
  );
}
