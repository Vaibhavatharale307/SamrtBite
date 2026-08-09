import axios from "axios";

// All main service calls go through /main-api proxy (Vite dev) → localhost:8081
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/main-api",
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 globally - auto logout
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("smartbiteUser");
      window.location.href = "/";   // → landing page, user picks their login type
    }
    return Promise.reject(error);
  }
);

export default apiClient;
