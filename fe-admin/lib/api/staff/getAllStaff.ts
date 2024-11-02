import {  StaffData, StaffFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getAllStaff(
  params: StaffFilterParam
): Promise<AxiosResponse<StaffData>> {
  return axios.get<StaffData>(`/admin/staff?${encodeParams(params)}`);
}
