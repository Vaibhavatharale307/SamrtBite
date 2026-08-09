export default function WalletCard({ balance }) {
  return (
    <article className="wallet-card">
      <p className="mb-2">Current Balance</p>
      <h2 className="display-6 mb-0">&#8377;{Number(balance || 0).toFixed(2)}</h2>
    </article>
  );
}
