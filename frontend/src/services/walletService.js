import apiClient from "./apiClient";

export const getWallet = (userId) => apiClient.get(`/wallet/${userId}`);
export const addMoney = (moneyData) => apiClient.put("/wallet/addMoney", moneyData);
export const getTransactions = (userId) => apiClient.get(`/wallet/${userId}/history`);
