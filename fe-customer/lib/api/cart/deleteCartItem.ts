import axios from "../axios";

export const deleteCartItem = (id: number) => {
  return axios.delete(`/cart/${id}`);
};
