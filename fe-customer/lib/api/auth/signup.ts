import { SignupPayload, SuccessResponse } from "@/lib/types";
import axios from "../axios";

export const signup = (payload: SignupPayload) => {
  return axios.post<SuccessResponse>("/auth/register", payload);
};
