import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import { useAuth } from "../context/AuthContext";
export default function Profile() {
  const { user } = useAuth();
  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <h1 className="h3">Profile</h1>
        <p className="text-muted">Your logged-in account information.</p>
        <section className="content-card profile-card">
          <div className="profile-avatar">
            {user?.name?.charAt(0)?.toUpperCase() || "S"}
          </div>
          <div>
            <h2 className="h4">{user?.name}</h2>
            <p className="mb-1">
              <strong>Email:</strong> {user?.email}
            </p>
            <p className="mb-1">
              <strong>User ID:</strong> {user?.userId}
            </p>
            <p className="mb-0">
              <strong>Role:</strong> {user?.role}
            </p>
          </div>
        </section>
      </main>
    </div>
  );
}
