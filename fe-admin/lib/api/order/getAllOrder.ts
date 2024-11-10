import { OrderData, OrderFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getAllOrder(
  params: OrderFilterParam,
  page: number
): Promise<AxiosResponse<OrderData>> {
  return axios.get<OrderData>(
    `/admin/order?${encodeParams({ ...params, page })}`
  );
}
