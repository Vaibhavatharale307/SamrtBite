import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");   // → landing page, not /login
  };

  return (
    <header className="top-navbar d-flex justify-content-between align-items-center px-4">
      <div className="d-flex align-items-center gap-2">
        <span className="brand-mark">S</span>
        <span className="fw-bold fs-5">SmartBite</span>
      </div>
      <div className="d-flex align-items-center gap-3">
        <span className="text-muted small d-none d-sm-inline">
          Hi, {user?.name || "Student"}
        </span>
        <button
          type="button"
          className="btn btn-outline-success btn-sm"
          onClick={handleLogout}
        >
          Logout
        </button>
      </div>
    </header>
  );
}
