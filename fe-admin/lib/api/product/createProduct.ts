import { CreateProductPayload, Product } from "@/lib/types";
import axios from "../axios";

export const createProduct = (payload: CreateProductPayload) => {
  return axios.post<Product>("/admin/item", payload);
};
