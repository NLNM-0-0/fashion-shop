import { SuccessResponse, VerifyOtpPayload } from "@/lib/types";
import axios from "../axios";

export const verifyOTP = (payload: VerifyOtpPayload) => {
  return axios.post<SuccessResponse>("/auth/verify", payload);
};
