import { ProductList } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getBestSeller(): Promise<AxiosResponse<ProductList>> {
  return axios.get<ProductList>(`/home/bestSeller`);
}
