import axios from "../axios";

export const cancelOrder = (id: string) => {
  return axios.delete(`/admin/order/${id}`);
};
