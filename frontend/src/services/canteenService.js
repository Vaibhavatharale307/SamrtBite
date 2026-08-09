import apiClient from "./apiClient";

export const getAllCanteens = () => apiClient.get("/canteen");
export const getMenuByCanteen = (canteenId) => apiClient.get(`/canteen/menu/${canteenId}`);
export const getMenuItem = (foodId) => apiClient.get(`/canteen/menu/item/${foodId}`);

export const getMenu = () => apiClient.get("/canteen/menu");
export const addFood = (foodData) => apiClient.post("/canteen/menu", foodData);
export const updateFood = (foodId, foodData) =>
  apiClient.put(`/canteen/menu/${foodId}`, foodData);
export const deleteFood = (foodId) => apiClient.delete(`/canteen/menu/${foodId}`);
