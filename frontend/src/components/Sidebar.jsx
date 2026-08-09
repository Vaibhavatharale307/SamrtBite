import { NavLink } from "react-router-dom";

const links = [
  { label: "Dashboard", path: "/dashboard" },
  { label: "Browse Canteens", path: "/canteens" },
  { label: "My Orders", path: "/orders" },
  { label: "Wallet", path: "/wallet" },
  { label: "Transactions", path: "/transactions" },
  { label: "Profile", path: "/profile" },
];

export default function Sidebar() {
  return (
    <aside className="sidebar p-3">
      <p className="sidebar-title">MAIN MENU</p>
      <nav className="nav flex-column gap-1">
        {links.map((link) => (
          <NavLink key={link.path} to={link.path} className="sidebar-link">
            {link.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
