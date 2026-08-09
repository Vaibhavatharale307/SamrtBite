export default function Navbar({ title, subtitle, onLogout }) {
  return (
    <div className="manager-topbar d-flex flex-wrap justify-content-between align-items-center gap-3 bg-white border rounded-4 px-4 py-3 shadow-sm">
      <div>
        <p className="text-uppercase text-success fw-semibold small mb-1">
          SmartBite Manager
        </p>
        <h4 className="mb-0">{title}</h4>
        {subtitle ? <div className="text-muted small">{subtitle}</div> : null}
      </div>

      {onLogout ? (
        <button className="btn btn-outline-success btn-sm px-3" onClick={onLogout}>
          Logout
        </button>
      ) : null}
    </div>
  );
}
