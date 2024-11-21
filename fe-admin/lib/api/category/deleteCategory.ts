import axios from "../axios";

export const deleteCategory = (id: number) => {
  return axios.delete(`/admin/category/${id}`);
};
