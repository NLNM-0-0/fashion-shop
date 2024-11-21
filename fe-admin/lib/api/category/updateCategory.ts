import axios from "../axios";
import { CreateCategoryPayload } from "./createCategory";

export const updateCategory = (id: number, payload: CreateCategoryPayload) => {
  return axios.put(`/admin/category/${id}`, payload);
};
