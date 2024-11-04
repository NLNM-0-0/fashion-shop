import { CreateStaffPayload } from "@/lib/types";
import axios from "../axios";

export const updateStaff = (id: number, payload: CreateStaffPayload) => {
  return axios.put(`/admin/staff/${id}`, payload);
};
