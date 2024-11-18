import axios from "../axios";

interface UpdateQuantityPayload {
  quantityChange?: number;
}
export const updateCartQuantity = (
  id: number,
  payload: UpdateQuantityPayload
) => {
  return axios.post(`/cart/${id}/quantity`, payload);
};
