export default function StatCard({ title, value, hint }) {
  return (
    <div className="card manager-card shadow-sm border-0 rounded-4 h-100">
      <div className="card-body p-4">
        <div className="text-muted small mb-2">{title}</div>
        <div className="display-6 fw-semibold text-success mb-1">{value}</div>
        {hint ? <div className="small text-secondary">{hint}</div> : null}
      </div>
    </div>
  );
}
