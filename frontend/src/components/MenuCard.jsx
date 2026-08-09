export default function MenuCard({
  menu,
  quantity,
  pickupSlot,
  pickupSlots,
  onQuantityChange,
  onSlotChange,
  onOrder,
}) {
  return (
    <article className="card menu-card h-100 border-0 shadow-sm">
      <img
        src="https://placehold.co/600x400/eaf6ef/198754?text=SmartBite+Food"
        className="card-img-top"
        alt={menu.foodName}
      />
      <div className="card-body d-flex flex-column">
        <h2 className="h5">{menu.foodName}</h2>
        <p className="text-success fw-bold mb-1">&#8377;{menu.price}</p>
        <p className="small mb-2">
          {menu.available ? (
            <span className="text-success">Available</span>
          ) : (
            <span className="text-danger">Not available</span>
          )}
        </p>

        {/* Pickup slot selector */}
        {menu.available && (
          <div className="mb-2">
            <label className="form-label small mb-1">Pickup Slot</label>
            <select
              className="form-select form-select-sm"
              value={pickupSlot}
              onChange={(e) => onSlotChange(menu.foodId, e.target.value)}
            >
              {pickupSlots.map((slot) => (
                <option key={slot} value={slot}>{slot}</option>
              ))}
            </select>
          </div>
        )}

        <div className="d-flex align-items-center gap-2 mt-auto">
          <label className="small">Qty</label>
          <input
            type="number"
            min="1"
            className="form-control quantity-input"
            value={quantity}
            onChange={(e) => onQuantityChange(menu.foodId, e.target.value)}
            disabled={!menu.available}
          />
          <button
            className="btn btn-success"
            onClick={() => onOrder(menu)}
            disabled={!menu.available}
          >
            Order
          </button>
        </div>
      </div>
    </article>
  );
}
