import { Product } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getProduct(
  id: string
): Promise<AxiosResponse<Product>> {
  return axios.get<Product>(`/admin/item/${id}`);
}
