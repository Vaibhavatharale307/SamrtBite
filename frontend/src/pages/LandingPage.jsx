import { Link } from "react-router-dom";

/* ─────────────────────────────────────────────────
   SmartBite Landing Page  —  Route: /
   Two login flows: /student-login & /manager-login
   Styled with Bootstrap 5 + styles/app.css (green theme).
───────────────────────────────────────────────── */

export default function LandingPage() {
  return (
    <div style={{ fontFamily: "'Inter', system-ui, sans-serif" }}>

      {/* ── HEADER ─────────────────────────────────── */}
      <header style={styles.header}>
        <div style={styles.headerInner}>

          {/* Brand */}
          <Link to="/" style={styles.brand} aria-label="SmartBite home">
            <span style={styles.brandIcon} aria-hidden="true">S</span>
            <span style={styles.brandText}>SmartBite</span>
          </Link>

          {/* Nav buttons */}
          <nav aria-label="Primary navigation" style={styles.nav}>
            <Link to="/student-login" className="btn btn-outline-success btn-sm" style={styles.btnOutline}>
              Student Login
            </Link>
            <Link to="/manager-login" className="btn btn-success btn-sm" style={styles.btnSolid}>
              Manager Login
            </Link>
          </nav>

        </div>
      </header>

      {/* ── HERO ───────────────────────────────────── */}
      <main>
        <section style={styles.hero} aria-labelledby="hero-heading">
          <div style={styles.heroGrid} className="landing-hero-grid">

            {/* Left column */}
            <div style={styles.heroLeft}>
              <span style={styles.badge}>🏫 Campus Dining Platform</span>

              <h1 id="hero-heading" style={styles.heroHeading}>
                Campus Dining,{" "}
                <span style={styles.heroAccent}>Made Smarter.</span>
              </h1>

              <p style={styles.heroDesc}>
                SmartBite is a campus dining platform that makes food ordering
                simple and convenient. Browse canteen menus, place orders,
                choose pickup slots, and manage payments through a digital
                wallet.
              </p>

              {/* CTA Buttons */}
              <div style={styles.ctaRow} className="landing-cta-row">
                <Link
                  to="/student-login"
                  className="btn btn-success"
                  style={styles.ctaPrimary}
                  aria-label="Login as Student"
                >
                  Login as Student
                </Link>
                <Link
                  to="/manager-login"
                  className="btn btn-outline-success"
                  style={styles.ctaSecondary}
                  aria-label="Login as Canteen Manager"
                >
                  Login as Canteen Manager
                </Link>
              </div>

              {/* Benefits */}
              <div style={styles.benefitsRow} className="landing-benefits-row" aria-label="Key benefits">
                {[
                  { icon: "✓", text: "Simple Ordering" },
                  { icon: "✓", text: "Smart Pickup" },
                  { icon: "✓", text: "Digital Wallet" },
                ].map((b) => (
                  <span key={b.text} style={styles.benefitChip}>
                    <span style={styles.benefitCheck}>{b.icon}</span> {b.text}
                  </span>
                ))}
              </div>
            </div>

            {/* Right column – hero image */}
            <div style={styles.heroRight} className="landing-hero-img-col" aria-hidden="true">
              <div style={styles.imgWrapper}>
                <img
                  src="/campus-canteen.jpg"
                  alt="Students enjoying food together at a campus canteen"
                  style={styles.heroImg}
                  loading="eager"
                />
              </div>
            </div>

          </div>
        </section>

        {/* ── HOW IT WORKS ───────────────────────────── */}
        <section style={styles.howSection} aria-labelledby="how-heading">
          <h2 id="how-heading" style={styles.sectionTitle}>
            How SmartBite Works
          </h2>
          <p style={styles.sectionSubtitle}>
            Three simple steps from hunger to happy.
          </p>

          <div style={styles.cardsGrid} className="landing-cards-grid">
            {[
              {
                step: "1",
                icon: "🍽️",
                title: "Browse Menu",
                desc: "Explore available food items from the campus canteen.",
              },
              {
                step: "2",
                icon: "🛒",
                title: "Place Order",
                desc: "Select your food, quantity and preferred pickup slot.",
              },
              {
                step: "3",
                icon: "💳",
                title: "Pay & Pickup",
                desc: "Pay using your digital wallet and collect your order at the selected slot.",
              },
            ].map((card) => (
              <article key={card.step} style={styles.card}>
                <div style={styles.cardStep}>{card.step}</div>
                <div style={styles.cardIcon}>{card.icon}</div>
                <h3 style={styles.cardTitle}>{card.title}</h3>
                <p style={styles.cardDesc}>{card.desc}</p>
              </article>
            ))}
          </div>
        </section>

        {/* ── CTA STRIP ──────────────────────────────── */}
        <section style={styles.ctaStrip} aria-label="Call to action">
          <h2 style={styles.ctaStripTitle}>Ready to order smarter?</h2>
          <p style={styles.ctaStripDesc}>
            Join hundreds of students already using SmartBite on campus.
          </p>
          <div style={styles.ctaRow}>
            <Link to="/register" className="btn" style={styles.ctaStripBtn}>
              Get Started — It&rsquo;s Free
            </Link>
            <Link
              to="/login"
              style={{ color: "rgba(255,255,255,.85)", fontSize: ".9rem" }}
            >
              Already have an account? Login →
            </Link>
          </div>
        </section>
      </main>

      {/* ── FOOTER ─────────────────────────────────── */}
      <footer style={styles.footer} role="contentinfo">
        <p style={{ margin: 0 }}>
          © 2026 SmartBite. Campus Dining Simplified.
        </p>
      </footer>

    </div>
  );
}

/* ─────────────────────────────────────────────────
   Inline styles — no extra CSS file needed.
   Uses the same green (#16a34a) as app.css tokens.
───────────────────────────────────────────────── */
const GREEN      = "#16a34a";
const GREEN_DARK = "#15803d";
const GREEN_PALE = "#f0fdf4";
const BORDER     = "#e2e8f0";
const TEXT       = "#0f172a";
const MUTE       = "#64748b";

const styles = {

  /* Header */
  header: {
    position: "sticky",
    top: 0,
    zIndex: 100,
    background: "#fff",
    borderBottom: `1px solid ${BORDER}`,
    boxShadow: "0 1px 3px rgba(0,0,0,.06)",
  },
  headerInner: {
    maxWidth: 1100,
    margin: "0 auto",
    padding: ".9rem 1.5rem",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: "1rem",
  },
  brand: {
    display: "flex",
    alignItems: "center",
    gap: ".55rem",
    textDecoration: "none",
    flexShrink: 0,
  },
  brandIcon: {
    display: "inline-flex",
    alignItems: "center",
    justifyContent: "center",
    width: 36,
    height: 36,
    borderRadius: 10,
    background: `linear-gradient(135deg, ${GREEN}, ${GREEN_DARK})`,
    color: "#fff",
    fontWeight: 800,
    fontSize: "1rem",
    boxShadow: "0 2px 8px rgba(22,163,74,.3)",
  },
  brandText: {
    fontWeight: 700,
    fontSize: "1.15rem",
    color: TEXT,
    letterSpacing: "-.01em",
  },
  nav: {
    display: "flex",
    gap: ".6rem",
    alignItems: "center",
    flexWrap: "wrap",
  },
  btnOutline: {
    borderRadius: 8,
    fontWeight: 500,
    fontSize: ".875rem",
  },
  btnSolid: {
    borderRadius: 8,
    fontWeight: 600,
    fontSize: ".875rem",
  },

  /* Hero */
  hero: {
    background: `linear-gradient(160deg, #fff 55%, ${GREEN_PALE} 100%)`,
    padding: "5rem 1.5rem 4rem",
  },
  heroGrid: {
    maxWidth: 1100,
    margin: "0 auto",
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    alignItems: "center",
    gap: "3rem",
  },
  heroLeft: {
    display: "flex",
    flexDirection: "column",
    gap: "1.25rem",
  },
  badge: {
    display: "inline-block",
    background: "#dcfce7",
    color: GREEN_DARK,
    fontSize: ".78rem",
    fontWeight: 700,
    letterSpacing: ".04em",
    borderRadius: 999,
    padding: ".3rem .85rem",
    alignSelf: "flex-start",
    textTransform: "uppercase",
  },
  heroHeading: {
    fontSize: "clamp(2rem, 4vw, 3rem)",
    fontWeight: 800,
    lineHeight: 1.15,
    color: TEXT,
    letterSpacing: "-.02em",
    margin: 0,
  },
  heroAccent: {
    color: GREEN,
  },
  heroDesc: {
    color: MUTE,
    lineHeight: 1.7,
    fontSize: "1.05rem",
    margin: 0,
    maxWidth: 480,
  },
  ctaRow: {
    display: "flex",
    alignItems: "center",
    gap: "1rem",
    flexWrap: "wrap",
  },
  ctaPrimary: {
    borderRadius: 10,
    fontWeight: 600,
    padding: ".65rem 1.75rem",
    fontSize: "1rem",
    boxShadow: "0 3px 10px rgba(22,163,74,.3)",
  },
  ctaSecondary: {
    borderRadius: 10,
    fontWeight: 600,
    padding: ".65rem 1.75rem",
    fontSize: "1rem",
  },
  benefitsRow: {
    display: "flex",
    gap: ".75rem",
    flexWrap: "wrap",
    marginTop: ".25rem",
  },
  benefitChip: {
    display: "inline-flex",
    alignItems: "center",
    gap: ".3rem",
    fontSize: ".85rem",
    fontWeight: 500,
    color: TEXT,
  },
  benefitCheck: {
    display: "inline-flex",
    alignItems: "center",
    justifyContent: "center",
    width: 20,
    height: 20,
    borderRadius: "50%",
    background: "#dcfce7",
    color: GREEN,
    fontSize: ".7rem",
    fontWeight: 800,
  },
  heroRight: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  imgWrapper: {
    borderRadius: 20,
    overflow: "hidden",
    boxShadow: "0 20px 40px rgba(0,0,0,.12)",
    border: `1px solid ${BORDER}`,
    lineHeight: 0,
    maxWidth: 520,
    width: "100%",
  },
  heroImg: {
    width: "100%",
    height: "auto",
    display: "block",
    objectFit: "cover",
  },

  /* How It Works */
  howSection: {
    background: "#fff",
    padding: "5rem 1.5rem",
    textAlign: "center",
  },
  sectionTitle: {
    fontSize: "clamp(1.5rem, 3vw, 2rem)",
    fontWeight: 700,
    color: TEXT,
    margin: "0 0 .5rem",
  },
  sectionSubtitle: {
    color: MUTE,
    fontSize: "1rem",
    margin: "0 0 3rem",
  },
  cardsGrid: {
    maxWidth: 960,
    margin: "0 auto",
    display: "grid",
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: "1.5rem",
  },
  card: {
    background: "#fff",
    border: `1px solid ${BORDER}`,
    borderRadius: 16,
    padding: "2rem 1.5rem",
    textAlign: "center",
    boxShadow: "0 2px 8px rgba(0,0,0,.05)",
    transition: "box-shadow .2s",
    position: "relative",
  },
  cardStep: {
    position: "absolute",
    top: "-14px",
    left: "50%",
    transform: "translateX(-50%)",
    background: GREEN,
    color: "#fff",
    fontWeight: 800,
    fontSize: ".8rem",
    width: 28,
    height: 28,
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  cardIcon: {
    fontSize: "2.25rem",
    marginBottom: ".75rem",
    display: "block",
  },
  cardTitle: {
    fontWeight: 700,
    fontSize: "1.05rem",
    color: TEXT,
    margin: "0 0 .5rem",
  },
  cardDesc: {
    color: MUTE,
    fontSize: ".9rem",
    lineHeight: 1.6,
    margin: 0,
  },

  /* CTA Strip */
  ctaStrip: {
    background: `linear-gradient(135deg, ${GREEN} 0%, ${GREEN_DARK} 100%)`,
    padding: "4.5rem 1.5rem",
    textAlign: "center",
    color: "#fff",
  },
  ctaStripTitle: {
    fontSize: "clamp(1.5rem, 3vw, 2.2rem)",
    fontWeight: 800,
    margin: "0 0 .75rem",
    color: "#fff",
  },
  ctaStripDesc: {
    color: "rgba(255,255,255,.85)",
    fontSize: "1.05rem",
    margin: "0 0 2rem",
  },
  ctaStripBtn: {
    background: "#fff",
    color: GREEN_DARK,
    fontWeight: 700,
    borderRadius: 10,
    padding: ".75rem 2rem",
    fontSize: "1rem",
    border: "none",
    boxShadow: "0 4px 14px rgba(0,0,0,.15)",
  },

  /* Footer */
  footer: {
    background: "#f8fafc",
    borderTop: `1px solid ${BORDER}`,
    padding: "1.75rem 1.5rem",
    textAlign: "center",
    color: MUTE,
    fontSize: ".875rem",
  },
};
