import { SuccessResponse, VerifyOtpPayload } from "@/lib/types";
import axios from "../axios";

export const verifyOTPForPassword = (payload: VerifyOtpPayload) => {
  return axios.post<SuccessResponse>("/auth/verify-reset-password", payload);
};
