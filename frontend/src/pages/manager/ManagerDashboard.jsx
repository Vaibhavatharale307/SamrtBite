import React from "react";

import ManagerSidebar from "../../components/manager/ManagerSidebar";
import ManagerNavbar from "../../components/manager/ManagerNavbar";
import StatCard from "../../components/manager/StatCard";

const ManagerDashboard = () => {
  const orders = [
    {
      id: 101,
      food: "Veg Thali",
      qty: 2,
      slot: "12:15 PM",
      status: "Preparing",
    },
    {
      id: 102,
      food: "Burger",
      qty: 1,
      slot: "12:30 PM",
      status: "Ready",
    },
    {
      id: 103,
      food: "Pizza",
      qty: 3,
      slot: "1:00 PM",
      status: "Completed",
    },
  ];

  return (
    <div className="d-flex">
      <ManagerSidebar />

      <div className="flex-grow-1 bg-light">
        <ManagerNavbar />

        <div className="container-fluid p-4">
          <div className="row g-4">
            <div className="col-md-3">
              <StatCard
                title="Today's Orders"
                value="25"
                icon="bi bi-cart-fill"
                color="primary"
              />
            </div>

            <div className="col-md-3">
              <StatCard
                title="Preparing"
                value="8"
                icon="bi bi-clock-history"
                color="warning"
              />
            </div>

            <div className="col-md-3">
              <StatCard
                title="Ready"
                value="10"
                icon="bi bi-check-circle-fill"
                color="success"
              />
            </div>

            <div className="col-md-3">
              <StatCard
                title="Completed"
                value="7"
                icon="bi bi-bag-check-fill"
                color="dark"
              />
            </div>
          </div>

          <div className="card mt-5 shadow-sm">
            <div className="card-header bg-success text-white">
              Recent Orders
            </div>

            <div className="card-body">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th>Order ID</th>

                    <th>Food</th>

                    <th>Qty</th>

                    <th>Pickup Slot</th>

                    <th>Status</th>
                  </tr>
                </thead>

                <tbody>
                  {orders.map((order) => (
                    <tr key={order.id}>
                      <td>{order.id}</td>

                      <td>{order.food}</td>

                      <td>{order.qty}</td>

                      <td>{order.slot}</td>

                      <td>
                        <span className="badge bg-success">{order.status}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;
