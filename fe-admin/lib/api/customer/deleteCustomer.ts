import axios from "../axios";

export const deleteCustomer= (id: number) => {
  return axios.delete(`/admin/customer/${id}`);
};
