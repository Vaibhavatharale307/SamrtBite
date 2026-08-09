import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import WalletCard from "../components/WalletCard";
import {
  addMoney,
  getTransactions,
  getWallet,
} from "../services/walletService";
import { useAuth } from "../context/AuthContext";
export default function Wallet() {
  const { user } = useAuth();
  const [wallet, setWallet] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const loadWallet = async () => {
    try {
      const [walletRes, transactionRes] = await Promise.all([
        getWallet(user.userId),
        getTransactions(user.userId),
      ]);
      setWallet(walletRes.data);
      setTransactions(transactionRes.data);
    } catch {
      setError("Unable to load wallet details.");
    }
  };
  useEffect(() => {
    loadWallet();
  }, [user.userId]);
  const handleAddMoney = async (e) => {
    e.preventDefault();
    try {
      await addMoney({ userId: user.userId, amount: Number(amount) });
      setAmount("");
      setMessage("Money added successfully.");
      loadWallet();
    } catch (apiError) {
      setError(apiError.response?.data?.message || "Unable to add money.");
    }
  };
  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <h1 className="h3">Wallet</h1>
        <p className="text-muted">Manage your digital wallet balance.</p>
        {message && <div className="alert alert-success">{message}</div>}
        {error && <div className="alert alert-danger">{error}</div>}
        <div className="row g-4">
          <div className="col-lg-5">
            <WalletCard balance={wallet?.balance} />
            <form className="content-card mt-4" onSubmit={handleAddMoney}>
              <h2 className="h5">Add Money</h2>
              <div className="input-group">
                <span className="input-group-text">&#8377;</span>
                <input
                  type="number"
                  min="1"
                  className="form-control"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  required
                />
                <button className="btn btn-success">Add</button>
              </div>
            </form>
          </div>
          <div className="col-lg-7">
            <div className="content-card">
              <h2 className="h5">Recent Transactions</h2>
              {transactions.length === 0 && (
                <p className="text-muted small">No transactions yet.</p>
              )}
              {transactions.slice(0, 10).map((transaction) => (
                <div className="border-bottom py-2" key={transaction.txnId || transaction.id}>
                  <strong>{transaction.description || "Transaction"}</strong>
                  <span
                    className={
                      transaction.type === "CREDIT"
                        ? "float-end text-success"
                        : "float-end text-danger"
                    }
                  >
                    {transaction.type === "CREDIT" ? "+" : "-"}&#8377;
                    {Number(transaction.amount || 0).toFixed(2)}
                  </span>
                  <div className="small text-muted">
                    {transaction.createdAt
                      ? new Date(transaction.createdAt).toLocaleString("en-IN")
                      : ""}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
