import apiClient from "./apiClient";

// ===============================
// LOGIN
// ===============================

export const loginUser = (credentials) => {
  return apiClient.post("/auth/login", credentials);
};

export const loginManager = loginUser;


// ===============================
// REGISTER
// ===============================

export const registerUser = (registrationData) => {
  return apiClient.post("/auth/register", registrationData);
};


// ===============================
// CURRENT USER
// ===============================

export const getLoggedInUser = () => {
  const rawUser = localStorage.getItem("smartbiteUser");

  return rawUser ? JSON.parse(rawUser) : null;
};


// ===============================
// LOGOUT
// ===============================

export const logoutUser = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("smartbiteUser");
};


// ===============================
// FORGOT PASSWORD
// ===============================

export const forgotPassword = (email) => {
  return apiClient.post("/auth/forgot-password", {
    email: email,
  });
};


// ===============================
// VERIFY OTP
// ===============================

export const verifyOtp = (email, otp) => {
  return apiClient.post("/auth/verify-otp", {
    email: email,
    otp: otp,
  });
};


// ===============================
// RESET PASSWORD
// ===============================

export const resetPassword = (email, newPassword) => {
  return apiClient.post("/auth/reset-password", {
    email: email,
    newPassword: newPassword,
  });
};