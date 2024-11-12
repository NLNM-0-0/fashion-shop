import { SendOtpPayload, SuccessResponse } from "@/lib/types";
import axios from "../axios";

export const sendOTP = (payload: SendOtpPayload) => {
  return axios.post<SuccessResponse>("/auth/send_otp", payload);
};
