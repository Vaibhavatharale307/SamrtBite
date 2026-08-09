import { Link, NavLink } from "react-router-dom";

const linkClass = ({ isActive }) =>
  `nav-link rounded-3 px-3 py-2 ${isActive ? "active bg-success text-white" : "text-dark"}`;

export default function Sidebar({ onLogout }) {
  return (
    <aside className="manager-sidebar bg-white border-end min-vh-100 p-3">
      <Link to="/manager/dashboard" className="text-decoration-none">
        <div className="fw-bold fs-4 text-success mb-4">SmartBite</div>
      </Link>

      <nav className="nav flex-column gap-2">
        <NavLink className={linkClass} to="/manager/dashboard">
          Dashboard
        </NavLink>
        <NavLink className={linkClass} to="/manager/orders">
          Orders
        </NavLink>
        <NavLink className={linkClass} to="/manager/menu">
          Manage Menu
        </NavLink>
        <NavLink className={linkClass} to="/manager/profile">
          Profile
        </NavLink>
      </nav>

      <button className="btn btn-outline-danger w-100 mt-4" onClick={onLogout}>
        Logout
      </button>
    </aside>
  );
}
