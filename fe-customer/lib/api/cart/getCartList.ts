import { CartListData } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getCartList(): Promise<AxiosResponse<CartListData>> {
  return axios.get<CartListData>(`/cart`);
}
