import axios, { AxiosError, AxiosResponse } from "axios";
import Router from "next/router";
import Cookies from "js-cookie";
import { ApiError } from "next/dist/server/api-utils";
import loadingEmitter from "./loading-emitter";

const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "https://api.example.com",
  headers: {
    "Cache-Control": "no-cache",
    accept: "*/*",
  },
});
apiClient.interceptors.request.use((config) => {
  const token = Cookies.get("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  if (config.method !== "get") {
    loadingEmitter.emit("loading", true);
  }

  return config;
});

apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    if (response.config.method !== "get") {
      loadingEmitter.emit("loading", false);
    }
    return response;
  },
  (error: AxiosError<ApiError>) => {
    if (error.response?.config.method !== "get") {
      loadingEmitter.emit("loading", false);
    }
    if (error.response?.status === 401) {
      Cookies.remove("token");
      Router.push("/login");
    }
    return Promise.reject(error);
  }
);

export default apiClient;
