import axios from "../axios";

export const deleteStaff = (id: number) => {
  return axios.delete(`/admin/staff/${id}`);
};
