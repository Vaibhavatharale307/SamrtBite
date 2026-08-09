import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { resetPassword } from "../services/authService";

function ResetPasswordPage() {
  const navigate = useNavigate();

  const email = sessionStorage.getItem("resetEmail");
  const verified = sessionStorage.getItem("otpVerified");

  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setSuccess("");

    if (!verified) {
      setError("Please verify OTP first.");
      return;
    }

    if (password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      setLoading(true);

      await resetPassword(email, password);

      setSuccess("Password reset successfully!");

      sessionStorage.removeItem("resetEmail");
      sessionStorage.removeItem("otpVerified");

      setTimeout(() => {
        navigate("/student-login");
      }, 1500);
    } catch (err) {
      console.error(err);

      setError(
        err?.response?.data?.message ||
          "Unable to reset password. Please try again.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="container-fluid min-vh-100 d-flex align-items-center justify-content-center bg-light">
      <section
        className="card shadow-sm border-0 p-4"
        style={{ width: "100%", maxWidth: "430px" }}
      >
        {/* Brand */}
        <Link to="/" className="text-decoration-none text-center">
          <div className="fw-bold fs-3 text-success">SmartBite</div>
        </Link>

        {/* Heading */}
        <h1 className="h3 mt-3 text-center">Reset Password</h1>

        <p className="text-muted text-center">
          Create a new password for your SmartBite account.
        </p>

        {/* Account Email */}
        <div className="alert alert-light border text-center">
          <small className="text-muted">Resetting password for</small>
          <br />
          <strong>{email}</strong>
        </div>

        {/* Error */}
        {error && (
          <div className="alert alert-danger text-start" role="alert">
            {error}
          </div>
        )}

        {/* Success */}
        {success && (
          <div className="alert alert-success text-start" role="alert">
            {success}
          </div>
        )}

        {/* Form */}
        <form className="text-start" onSubmit={handleSubmit}>
          {/* New Password */}
          <div className="mb-3">
            <label className="form-label" htmlFor="new-password">
              New Password
            </label>

            <input
              id="new-password"
              type="password"
              className="form-control"
              placeholder="Enter new password"
              autoComplete="new-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          {/* Confirm Password */}
          <div className="mb-3">
            <label className="form-label" htmlFor="confirm-password">
              Confirm Password
            </label>

            <input
              id="confirm-password"
              type="password"
              className="form-control"
              placeholder="Confirm new password"
              autoComplete="new-password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>

          {/* Reset Button */}
          <button
            type="submit"
            className="btn btn-success w-100"
            disabled={loading}
          >
            {loading ? "Resetting..." : "Reset Password"}
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

export default ResetPasswordPage;
