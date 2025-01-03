import { ProductData, ProductFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getAllProduct(
  params: ProductFilterParam
): Promise<AxiosResponse<ProductData>> {
  return axios.get<ProductData>(
    `/item?${encodeParams({ ...params, limit: 12 })}`
  );
}
