import axios from "../axios";

interface CreateOrderPayload {
  name: string;
  phone: string;
  address: string;
  cardIds: number[];
}
export const createOrder = (payload: CreateOrderPayload) => {
  return axios.post(`/order`, payload);
};
