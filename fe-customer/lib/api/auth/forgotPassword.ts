import { ForgotPasswordPayload, SuccessResponse } from "@/lib/types";
import axios from "../axios";

export const forgotPassword = (payload: ForgotPasswordPayload) => {
  return axios.post<SuccessResponse>("/auth/reset_password", payload);
};
