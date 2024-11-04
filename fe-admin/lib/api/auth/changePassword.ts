import { ChangePasswordPayload } from "@/lib/types";
import axios from "../axios";

export const changePassword = (payload: ChangePasswordPayload) => {
  return axios.post("/admin/user/password", payload);
};
