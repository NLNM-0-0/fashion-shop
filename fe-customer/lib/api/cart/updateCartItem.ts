import axios from "../axios";

interface UpdateCartItemPayload {
  size?: string;
  color?: string;
  quantity?: number;
}
export const updateCartItem = (id: number, payload: UpdateCartItemPayload) => {
  return axios.post(`/cart/${id}`, payload);
};
