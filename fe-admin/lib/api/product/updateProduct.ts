import { CreateProductPayload, Product } from "@/lib/types";
import axios from "../axios";

export const updateProduct = (id: number, payload: CreateProductPayload) => {
  return axios.put<Product>(`/admin/item/${id}`, payload);
};
