import { LoginPayload, LoginToken } from "@/lib/types";
import axios from "../axios";
import Cookies from "js-cookie";

export const login = (payload: LoginPayload) => {
  return axios
    .post<LoginToken>("/admin/auth/authenticate", payload)
    .then((res) => {
      Cookies.set("token", res.data.token, {
        sameSite: "Strict",
      });
    });
};
