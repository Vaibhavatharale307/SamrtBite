export default function DashboardCard({ title, value, note }) {
  return (
    <article className="dashboard-card h-100">
      <p className="text-muted mb-2">{title}</p>
      <h3 className="mb-2">{value}</h3>
      <small className="text-success">{note}</small>
    </article>
  );
}
