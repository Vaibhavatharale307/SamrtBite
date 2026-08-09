import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidebar from "../../components/manager/Sidebar";
import Navbar from "../../components/manager/Navbar";
import { addFood } from "../../services/canteenService";
import { getLoggedInUser, logoutUser } from "../../services/authService";

const initialState = {
  foodName: "",
  description: "",
  price: "",
  category: "",
  imageUrl: "",
  available: true,
};

export default function AddFood() {
  const navigate = useNavigate();
  const user = getLoggedInUser();
  const canteenId = user?.canteenId || user?.canteen?.canteenId || user?.canteen?.id;
  const [formData, setFormData] = useState(initialState);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;
    setFormData((prev) => ({ ...prev, [name]: type === "checkbox" ? checked : value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!formData.foodName || !formData.price || !formData.category) {
      setError("Please fill all required fields.");
      return;
    }
    setLoading(true);
    try {
      setError("");
      await addFood({ ...formData, canteenId, price: Number(formData.price) });
      setMessage("Food item added successfully.");
      setTimeout(() => navigate("/manager/menu"), 1000);
    } catch (err) {
      setError(err?.response?.data?.message || "Failed to add food item.");
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logoutUser();
    navigate("/manager/login");
  };

  return (
    <div className="manager-shell d-flex">
      <Sidebar onLogout={handleLogout} />
      <main className="flex-grow-1 p-3 p-md-4">
        <Navbar title="Add Food" subtitle="Create a new menu item" onLogout={handleLogout} />

        {message ? <div className="alert alert-success mt-3">{message}</div> : null}
        {error ? <div className="alert alert-danger mt-3">{error}</div> : null}

        <div className="card rounded-4 shadow-sm border-0 mt-3">
          <div className="card-body p-4">
            <form onSubmit={handleSubmit}>
              <div className="row g-3">
                <div className="col-md-6">
                  <label className="form-label">Food Name</label>
                  <input className="form-control" name="foodName" value={formData.foodName} onChange={handleChange} required />
                </div>
                <div className="col-md-6">
                  <label className="form-label">Category</label>
                  <input className="form-control" name="category" value={formData.category} onChange={handleChange} required />
                </div>
                <div className="col-12">
                  <label className="form-label">Description</label>
                  <textarea className="form-control" name="description" rows="4" value={formData.description} onChange={handleChange} />
                </div>
                <div className="col-md-4">
                  <label className="form-label">Price</label>
                  <input type="number" min="0" step="0.01" className="form-control" name="price" value={formData.price} onChange={handleChange} required />
                </div>
                <div className="col-md-8">
                  <label className="form-label">Image URL</label>
                  <input className="form-control" name="imageUrl" value={formData.imageUrl} onChange={handleChange} />
                </div>
                <div className="col-12">
                  <div className="form-check">
                    <input className="form-check-input" type="checkbox" name="available" checked={formData.available} onChange={handleChange} id="available" />
                    <label className="form-check-label" htmlFor="available">Available</label>
                  </div>
                </div>
              </div>
              <div className="mt-4 d-flex gap-2">
                <button className="btn btn-success" disabled={loading}>
                  {loading ? "Submitting..." : "Submit"}
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={() => navigate("/manager/menu")}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </main>
    </div>
  );
}
