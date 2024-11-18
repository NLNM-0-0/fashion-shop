import { AddToCartPayload, SuccessResponse } from "@/lib/types";
import axios from "../axios";

export const addToCard = (payload: AddToCartPayload) => {
  return axios.post<SuccessResponse>("/cart", payload);
};
