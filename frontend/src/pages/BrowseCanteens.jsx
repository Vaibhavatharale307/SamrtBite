import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import { getAllCanteens } from "../services/canteenService";
export default function BrowseCanteens() {
  const [canteens, setCanteens] = useState([]);

  const [error, setError] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    getAllCanteens()
      .then((r) => setCanteens(r.data))
      .catch(() => setError("Unable to load canteens."));
  }, []);

  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <h1 className="h3">Browse Canteens</h1>
        <p className="text-muted">Choose a canteen to view its menu.</p>
        {error && <div className="alert alert-danger">{error}</div>}
        <div className="row g-4">
          {canteens.map((c) => (
            <div className="col-md-6 col-xl-4" key={c.canteenId}>
              <article className="content-card h-100">
                <h2 className="h5">{c.canteenName}</h2>
                <p className="mb-1">Opening: {c.openingTime}</p>
                <p className="mb-3">Closing: {c.closingTime}</p>
                <p className={c.active ? "text-success" : "text-danger"}>
                  {c.active ? "Active" : "Inactive"}
                </p>
                <button
                  className="btn btn-success"
                  disabled={!c.active}
                  onClick={() => navigate(`/canteens/${c.canteenId}/menu`)}
                >
                  View Menu
                </button>
              </article>
            </div>
          ))}
        </div>
        {!canteens.length && !error && (
          <p className="text-muted">No canteens are available.</p>
        )}
      </main>
    </div>
  );
}
