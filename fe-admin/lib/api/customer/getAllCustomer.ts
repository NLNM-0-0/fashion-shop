import { CustomerData, CustomerFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getAllCustomer(
  params: CustomerFilterParam
): Promise<AxiosResponse<CustomerData>> {
  return axios.get<CustomerData>(`/admin/customer?${encodeParams(params)}`);
}
