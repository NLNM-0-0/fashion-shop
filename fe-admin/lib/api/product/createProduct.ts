import { CreateProductPayload, Staff } from "@/lib/types";
import axios from "../axios";

export const createProduct = (payload: CreateProductPayload) => {
  return axios.post<Staff>("/admin/item", payload);
};
