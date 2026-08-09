import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { verifyOtp } from "../services/authService";

export default function VerifyOtpPage() {
  const navigate = useNavigate();

  const email = sessionStorage.getItem("resetEmail");

  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!otp) {
      setError("Please enter OTP.");
      return;
    }

    if (otp.length !== 6) {
      setError("Please enter a valid 6-digit OTP.");
      return;
    }

    try {
      setLoading(true);

      await verifyOtp(email, otp);

      sessionStorage.setItem("otpVerified", "true");

      navigate("/reset-password");
    } catch (err) {
      console.error(err);

      setError(
        err?.response?.data?.message ||
          "Invalid or expired OTP. Please try again.",
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
        <h1 className="h3 mt-3 text-center">Verify OTP</h1>

        <p className="text-muted text-center">
          Enter the 6-digit OTP sent to your registered email address.
        </p>

        {/* Email */}
        <div className="alert alert-light border text-center">
          <small className="text-muted">OTP sent to</small>
          <br />
          <strong>{email}</strong>
        </div>

        {/* Error */}
        {error && (
          <div className="alert alert-danger text-start" role="alert">
            {error}
          </div>
        )}

        {/* Form */}
        <form className="text-start" onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label" htmlFor="verify-otp">
              One-Time Password
            </label>

            <input
              id="verify-otp"
              type="text"
              className="form-control text-center"
              placeholder="Enter 6-digit OTP"
              maxLength="6"
              inputMode="numeric"
              autoComplete="one-time-code"
              value={otp}
              onChange={(e) => setOtp(e.target.value.replace(/\D/g, ""))}
              required
            />
          </div>

          <button
            type="submit"
            className="btn btn-success w-100"
            disabled={loading}
          >
            {loading ? "Verifying..." : "Verify OTP"}
          </button>
        </form>

        <hr className="my-3" />

        {/* Back */}
        <Link
          to="/forgot-password"
          className="btn btn-outline-success w-100 btn-sm"
        >
          ← Back to Forgot Password
        </Link>
      </section>
    </main>
  );
}
