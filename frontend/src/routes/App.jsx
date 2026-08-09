import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";

import ProtectedRoute from "./ProtectedRoute";

// Public Pages
import LandingPage from "../pages/LandingPage";
import Login from "../pages/Login";
import Register from "../pages/Register";
import StudentLogin from "../pages/student/StudentLogin";
import ManagerLoginPage from "../pages/manager/ManagerLoginPage";

// Password Reset Pages
import ForgotPasswordPage from "../pages/ForgotPasswordPage";
import VerifyOtpPage from "../pages/VerifyOtpPage";
import ResetPasswordPage from "../pages/ResetPasswordPage";

// Student Pages
import Dashboard from "../pages/Dashboard";
import BrowseCanteens from "../pages/BrowseCanteens";
import Menu from "../pages/Menu";
import MyOrders from "../pages/MyOrders";
import Wallet from "../pages/Wallet";
import Transactions from "../pages/Transactions";
import Profile from "../pages/Profile";

// Manager Pages
import ManagerLogin from "../pages/manager/Login";
import ManagerDashboard from "../pages/manager/Dashboard";
import ManagerOrders from "../pages/manager/Orders";
import ManageMenu from "../pages/manager/ManageMenu";
import AddFood from "../pages/manager/AddFood";
import EditFood from "../pages/manager/EditFood";
import ManagerProfile from "../pages/manager/Profile";

// Admin Pages
import AdminDashboard from "../pages/admin/Dashboard";
import AdminManageCanteens from "../pages/admin/ManageCanteens";
import AdminManageUsers from "../pages/admin/ManageUsers";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* =========================
            PUBLIC ROUTES
        ========================== */}

        {/* Landing Page */}
        <Route path="/" element={<LandingPage />} />

        {/* Student Login */}
        <Route path="/student-login" element={<StudentLogin />} />

        {/* Manager Login */}
        <Route path="/manager-login" element={<ManagerLoginPage />} />

        {/* Legacy Login */}
        <Route path="/login" element={<Login />} />

        {/* Legacy Manager Login */}
        <Route path="/manager/login" element={<ManagerLoginPage />} />

        {/* Registration */}
        <Route path="/register" element={<Register />} />

        {/* =========================
            FORGOT PASSWORD
        ========================== */}

        <Route path="/forgot-password" element={<ForgotPasswordPage />} />

        <Route path="/verify-otp" element={<VerifyOtpPage />} />

        <Route path="/reset-password" element={<ResetPasswordPage />} />

        {/* =========================
            STUDENT ROUTES
        ========================== */}

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute role="STUDENT">
              <Dashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/canteens"
          element={
            <ProtectedRoute role="STUDENT">
              <BrowseCanteens />
            </ProtectedRoute>
          }
        />

        <Route
          path="/canteens/:canteenId/menu"
          element={
            <ProtectedRoute role="STUDENT">
              <Menu />
            </ProtectedRoute>
          }
        />

        <Route
          path="/orders"
          element={
            <ProtectedRoute role="STUDENT">
              <MyOrders />
            </ProtectedRoute>
          }
        />

        <Route
          path="/wallet"
          element={
            <ProtectedRoute role="STUDENT">
              <Wallet />
            </ProtectedRoute>
          }
        />

        <Route
          path="/transactions"
          element={
            <ProtectedRoute role="STUDENT">
              <Transactions />
            </ProtectedRoute>
          }
        />

        <Route
          path="/profile"
          element={
            <ProtectedRoute role="STUDENT">
              <Profile />
            </ProtectedRoute>
          }
        />

        {/* =========================
            MANAGER ROUTES
        ========================== */}

        <Route
          path="/manager/dashboard"
          element={
            <ProtectedRoute role="CANTEEN_MANAGER">
              <ManagerDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/manager/orders"
          element={
            <ProtectedRoute role="CANTEEN_MANAGER">
              <ManagerOrders />
            </ProtectedRoute>
          }
        />

        <Route
          path="/manager/menu"
          element={
            <ProtectedRoute role="CANTEEN_MANAGER">
              <ManageMenu />
            </ProtectedRoute>
          }
        />

        <Route
          path="/manager/menu/add"
          element={
            <ProtectedRoute role="CANTEEN_MANAGER">
              <AddFood />
            </ProtectedRoute>
          }
        />

        <Route
          path="/manager/menu/edit/:id"
          element={
            <ProtectedRoute role="CANTEEN_MANAGER">
              <EditFood />
            </ProtectedRoute>
          }
        />

        <Route
          path="/manager/profile"
          element={
            <ProtectedRoute role="CANTEEN_MANAGER">
              <ManagerProfile />
            </ProtectedRoute>
          }
        />

        {/* =========================
            ADMIN ROUTES
        ========================== */}

        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/canteens"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminManageCanteens />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/users"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminManageUsers />
            </ProtectedRoute>
          }
        />

        {/* =========================
            DEFAULT ROUTE
        ========================== */}

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
