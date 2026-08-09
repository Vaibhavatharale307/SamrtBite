import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import MenuCard from "../components/MenuCard";
import { getMenuByCanteen } from "../services/canteenService";
import { placeOrder } from "../services/orderService";
import { useAuth } from "../context/AuthContext";

/* Pickup slot options shown to student before ordering */
const PICKUP_SLOTS = [
  "09:00 AM - 09:30 AM",
  "09:30 AM - 10:00 AM",
  "10:00 AM - 10:30 AM",
  "12:00 PM - 12:30 PM",
  "12:30 PM - 01:00 PM",
  "01:00 PM - 01:30 PM",
  "04:00 PM - 04:30 PM",
  "04:30 PM - 05:00 PM",
];

export default function Menu() {
  const { canteenId } = useParams();
  const { user } = useAuth();
  const [menu, setMenu]             = useState([]);
  const [quantities, setQuantities] = useState({});
  const [slots, setSlots]           = useState({});       // per-item slot selection
  const [message, setMessage]       = useState("");
  const [error, setError]           = useState("");
  const [loading, setLoading]       = useState(true);

  useEffect(() => {
    getMenuByCanteen(canteenId)
      .then((r) => setMenu(r.data || []))
      .catch(() => setError("Unable to load this menu."))
      .finally(() => setLoading(false));
  }, [canteenId]);

  const updateQuantity = (id, value) =>
    setQuantities({ ...quantities, [id]: Math.max(1, Number(value)) });

  const updateSlot = (id, value) =>
    setSlots({ ...slots, [id]: value });

  const orderFood = async (item) => {
    setMessage("");
    setError("");

    const pickupSlot = slots[item.foodId] || PICKUP_SLOTS[0];

    try {
      await placeOrder({
        userId:     user.userId,
        canteenId:  Number(canteenId),
        foodId:     item.foodId,
        quantity:   quantities[item.foodId] || 1,
        pickupSlot: pickupSlot,   // ← FIX: was missing, causing 400 Bad Request
      });
      setMessage(`✓ ${item.foodName} ordered! Pickup: ${pickupSlot}`);
    } catch (apiError) {
      const msg = apiError.response?.data?.message;
      if (apiError.response?.status === 400) {
        setError("Order failed: " + (msg || "Invalid order details."));
      } else if (apiError.response?.status === 409) {
        setError(msg || "Insufficient wallet balance or slot full.");
      } else {
        setError(msg || "Order could not be placed. Please try again.");
      }
    }
  };

  return (
    <div className="app-shell">
      <Navbar />
      <Sidebar />
      <main className="page-content">
        <h1 className="h3">Menu</h1>
        <p className="text-muted">Select quantity, choose a pickup slot and place your order.</p>

        {message && <div className="alert alert-success">{message}</div>}
        {error   && <div className="alert alert-danger">{error}</div>}

        {loading ? (
          <div className="text-center py-5">
            <div className="spinner-border text-success" role="status" />
          </div>
        ) : (
          <div className="row g-4">
            {menu.length === 0 && (
              <p className="text-muted">No menu items found for this canteen.</p>
            )}
            {menu.map((item) => (
              <div className="col-md-6 col-xl-4" key={item.foodId}>
                <MenuCard
                  menu={item}
                  quantity={quantities[item.foodId] || 1}
                  pickupSlot={slots[item.foodId] || PICKUP_SLOTS[0]}
                  pickupSlots={PICKUP_SLOTS}
                  onQuantityChange={updateQuantity}
                  onSlotChange={updateSlot}
                  onOrder={orderFood}
                />
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}
