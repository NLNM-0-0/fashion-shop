import axios, { AxiosError, AxiosResponse } from "axios";
import Router from "next/router";
import Cookies from "js-cookie";

const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "https://api.example.com",
  headers: {
    "Content-Type": "application/json",
    "Cache-Control": "no-cache",
    accept: "*/*",
  },
});
apiClient.interceptors.request.use((config) => {
  const token = Cookies.get("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      Cookies.remove("token");
      Router.push("/login");
    }
    return Promise.reject(error);
  }
);

export default apiClient;
