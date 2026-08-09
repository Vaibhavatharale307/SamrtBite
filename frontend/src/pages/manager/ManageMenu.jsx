import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidebar from "../../components/manager/Sidebar";
import Navbar from "../../components/manager/Navbar";
import { deleteFood, getMenuByCanteen } from "../../services/canteenService";
import { getLoggedInUser, logoutUser } from "../../services/authService";

export default function ManageMenu() {
  const navigate = useNavigate();
  const user = getLoggedInUser();
  const canteenId = user?.canteenId || user?.canteen?.canteenId || user?.canteen?.id;
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const loadMenu = async () => {
    try {
      const response = await getMenuByCanteen(canteenId);
      setItems(response.data || []);
    } catch (err) {
      setError(err?.response?.data?.message || "Unable to load menu.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!canteenId) {
      setError("Canteen details are missing from the logged-in account.");
      setLoading(false);
      return;
    }
    loadMenu();
  }, [canteenId]);

  const handleLogout = () => {
    logoutUser();
    navigate("/manager/login");
  };

  const handleDelete = async (foodId) => {
    const confirmed = window.confirm("Are you sure you want to delete this food item?");
    if (!confirmed) return;
    try {
      setMessage("");
      setError("");
      await deleteFood(foodId);
      setMessage("Food item deleted successfully.");
      await loadMenu();
    } catch (err) {
      setError(err?.response?.data?.message || "Failed to delete food item.");
    }
  };

  return (
    <div className="manager-shell d-flex">
      <Sidebar onLogout={handleLogout} />
      <main className="flex-grow-1 p-3 p-md-4">
        <Navbar
          title="Manage Menu"
          subtitle="Add, edit and remove food items"
          onLogout={handleLogout}
        />

        <div className="d-flex justify-content-end mt-3">
          <button className="btn btn-success" onClick={() => navigate("/manager/menu/add")}>
            Add Food
          </button>
        </div>

        {message ? <div className="alert alert-success mt-3">{message}</div> : null}
        {error ? <div className="alert alert-danger mt-3">{error}</div> : null}

        <div className="card rounded-4 shadow-sm border-0 mt-3">
          <div className="card-body">
            {loading ? (
              <div className="text-center py-5">
                <div className="spinner-border text-success" role="status" />
              </div>
            ) : (
              <div className="table-responsive">
                <table className="table align-middle">
                  <thead>
                    <tr>
                      <th>Image</th>
                      <th>Food Name</th>
                      <th>Category</th>
                      <th>Price</th>
                      <th>Available</th>
                      <th>Edit</th>
                      <th>Delete</th>
                    </tr>
                  </thead>
                  <tbody>
                    {items.map((item) => (
                      <tr key={item.foodId}>
                        <td>
                          <img
                            src={item.imageUrl || "https://via.placeholder.com/60"}
                            alt={item.foodName}
                            width="60"
                            height="60"
                            className="rounded-3 object-fit-cover"
                          />
                        </td>
                        <td>{item.foodName}</td>
                        <td>{item.category || "-"}</td>
                        <td>₹{Number(item.price || 0).toFixed(2)}</td>
                        <td>{item.available ? "Yes" : "No"}</td>
                        <td>
                          <button
                            className="btn btn-sm btn-outline-success"
                            onClick={() => navigate(`/manager/menu/edit/${item.foodId}`)}
                          >
                            Edit
                          </button>
                        </td>
                        <td>
                          <button
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDelete(item.foodId)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
