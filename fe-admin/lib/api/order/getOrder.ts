import { Order } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getOrder(
  orderId: string
): Promise<AxiosResponse<Order>> {
  return axios.get<Order>(`/admin/order/${orderId}`);
}
