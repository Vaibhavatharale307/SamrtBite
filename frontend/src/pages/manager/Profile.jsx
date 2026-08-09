import { useNavigate } from "react-router-dom";
import Sidebar from "../../components/manager/Sidebar";
import Navbar from "../../components/manager/Navbar";
import { getLoggedInUser, logoutUser } from "../../services/authService";

export default function Profile() {
  const navigate = useNavigate();
  const user = getLoggedInUser();

  const handleLogout = () => {
    logoutUser();
    navigate("/manager/login");
  };

  return (
    <div className="manager-shell d-flex">
      <Sidebar onLogout={handleLogout} />
      <main className="flex-grow-1 p-3 p-md-4">
        <Navbar title="Profile" subtitle="Manager account details" onLogout={handleLogout} />

        <div className="card rounded-4 shadow-sm border-0 mt-3">
          <div className="card-body p-4">
            <div className="row g-3">
              <div className="col-md-4">
                <label className="form-label text-muted">Manager Name</label>
                <div className="fw-semibold">{user?.name || user?.fullName || "-"}</div>
              </div>
              <div className="col-md-4">
                <label className="form-label text-muted">Email</label>
                <div className="fw-semibold">{user?.email || "-"}</div>
              </div>
              <div className="col-md-4">
                <label className="form-label text-muted">Phone</label>
                <div className="fw-semibold">{user?.phone || "-"}</div>
              </div>
            </div>

            <div className="mt-4">
              <button className="btn btn-outline-success" type="button">
                Change Password
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
