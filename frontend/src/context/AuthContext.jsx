import { createContext, useContext, useState, useCallback } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const raw = localStorage.getItem("smartbiteUser");
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  });

  const [token, setToken] = useState(() => localStorage.getItem("token"));

  const login = useCallback((userData, jwtToken) => {
    localStorage.setItem("smartbiteUser", JSON.stringify(userData));
    localStorage.setItem("token", jwtToken);
    setUser(userData);
    setToken(jwtToken);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem("smartbiteUser");
    localStorage.removeItem("token");
    setUser(null);
    setToken(null);
  }, []);

  const isAuthenticated = !!token && !!user;
  const isManager = user?.role === "CANTEEN_MANAGER";
  const isAdmin = user?.role === "ADMIN";
  const isStudent = user?.role === "STUDENT";

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAuthenticated, isManager, isAdmin, isStudent }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
