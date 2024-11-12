import { UserInfo } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getUser(): Promise<AxiosResponse<UserInfo>> {
  return axios.get<UserInfo>(`/admin/user`);
}
