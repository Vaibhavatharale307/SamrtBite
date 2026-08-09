import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
}

export default function ProtectedRoute({ children, role }) {
  const { token, user } = useAuth();

  // Not authenticated — send to landing page
  if (!token || isTokenExpired(token)) {
    return <Navigate to="/" replace />;
  }

  // Wrong role — redirect to the correct portal
  if (role && user?.role !== role) {
    if (user?.role === "CANTEEN_MANAGER") return <Navigate to="/manager/dashboard" replace />;
    if (user?.role === "ADMIN")           return <Navigate to="/admin/dashboard" replace />;
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}
