import axios from "../axios";

export const updateOrderStatus = (id: string) => {
  return axios.put(`/admin/order/${id}`);
};
