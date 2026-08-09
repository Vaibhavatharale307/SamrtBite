import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import { getTransactions } from "../services/walletService";
import { useAuth } from "../context/AuthContext";

export default function Transactions() {
  const { user } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getTransactions(user.userId)
      .then((r) => setTransactions(r.data || []))
      .catch(() => setError("Unable to load transactions."))
      .finally(() => setLoading(false));
  }, [user.userId]);

  const formatDate = (dateStr) => {
    if (!dateStr) return "—";
    try {
      return new Date(dateStr).toLocaleString("en-IN", {
        day: "2-digit", month: "short", year: "numeric",
        hour: "2-digit", minute: "2-digit",
      });
    } catch {
      return "—";
    }
  };

  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <h1 className="h3">Transactions</h1>
        <p className="text-muted">Complete wallet transaction history.</p>

        {error && <div className="alert alert-danger">{error}</div>}

        {loading ? (
          <div className="text-center py-5">
            <div className="spinner-border text-success" role="status" />
          </div>
        ) : (
          <div className="content-card table-responsive">
            <table className="table align-middle mb-0">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Description</th>
                  <th>Type</th>
                  <th className="text-end">Amount</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((t, idx) => (
                  <tr key={t.txnId || t.id || idx}>
                    <td className="small">{formatDate(t.createdAt)}</td>
                    <td>{t.description || "—"}</td>
                    <td>
                      <span className={`badge ${t.type === "CREDIT" ? "bg-success" : "bg-danger"}`}>
                        {t.type}
                      </span>
                    </td>
                    <td className={t.type === "CREDIT" ? "text-end text-success fw-bold" : "text-end text-danger fw-bold"}>
                      {t.type === "CREDIT" ? "+" : "-"}&#8377;{Number(t.amount || 0).toFixed(2)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {!transactions.length && !error && (
              <p className="text-muted mb-0 p-3">No transactions found.</p>
            )}
          </div>
        )}
      </main>
    </div>
  );
}
