import axios from "../axios";

interface CreateOrderItem {
  itemId: number;
  size: string;
  color: string;
  quantity: number;
}

interface CreateOrderPayload {
  details: CreateOrderItem[];
}
export const createOrder = (payload: CreateOrderPayload) => {
  return axios.post(`/order`, payload);
};
