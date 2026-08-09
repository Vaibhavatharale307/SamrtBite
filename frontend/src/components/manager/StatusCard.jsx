import React from "react";

const StatCard = ({ title, value, icon, color }) => {
  return (
    <div className="card shadow-sm border-0">
      <div className="card-body d-flex justify-content-between align-items-center">
        <div>
          <h6 className="text-muted">{title}</h6>
          <h3 className="fw-bold">{value}</h3>
        </div>

        <div
          className={`rounded-circle bg-${color} text-white d-flex justify-content-center align-items-center`}
          style={{
            width: "55px",
            height: "55px",
            fontSize: "22px",
          }}
        >
          <i className={icon}></i>
        </div>
      </div>
    </div>
  );
};

export default StatCard;
