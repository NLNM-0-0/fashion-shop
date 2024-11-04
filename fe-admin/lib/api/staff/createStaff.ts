import { CreateStaffPayload, Staff } from "@/lib/types";
import axios from "../axios";

export const createStaff = (payload: CreateStaffPayload) => {
  return axios.post<Staff>("/admin/staff", payload);
};
