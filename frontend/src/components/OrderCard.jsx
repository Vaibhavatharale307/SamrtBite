export default function OrderCard({ order, onCancel }) {
  const canCancel = order.status === "PLACED" || order.status === "PREPARING";

  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    try {
      return new Date(dateStr).toLocaleString("en-IN", {
        day: "2-digit", month: "short", year: "numeric",
        hour: "2-digit", minute: "2-digit",
      });
    } catch {
      return "";
    }
  };

  const statusColor = {
    PLACED:    "bg-primary",
    PREPARING: "bg-warning text-dark",
    READY:     "bg-info text-dark",
    COMPLETED: "bg-success",
    CANCELLED: "bg-secondary",
  };

  return (
    <article className="content-card mb-3 d-flex flex-column flex-md-row justify-content-between gap-3">
      <div>
        <h2 className="h6 mb-1">{order.foodName || `Food #${order.foodId}`}</h2>
        <p className="small text-muted mb-1">
          Qty: {order.quantity}
          {order.pickupSlot ? ` · Pickup: ${order.pickupSlot}` : ""}
          {order.createdAt ? ` · ${formatDate(order.createdAt)}` : ""}
        </p>
        <strong>&#8377;{Number(order.totalAmount || 0).toFixed(2)}</strong>
      </div>
      <div className="text-md-end">
        <span className={`badge ${statusColor[order.status] || "bg-secondary"}`}>
          {order.status}
        </span>
        {canCancel && (
          <button
            className="btn btn-outline-danger btn-sm d-block mt-2 ms-md-auto"
            onClick={() => onCancel(order.orderId)}
          >
            Cancel Order
          </button>
        )}
      </div>
    </article>
  );
}
