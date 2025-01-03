import axios from "../axios";

export const deleteProduct = (id: number) => {
  return axios.delete(`/admin/item/${id}`);
};
