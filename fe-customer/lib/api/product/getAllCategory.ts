import { CategoryData } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getAllCategory(): Promise<AxiosResponse<CategoryData>> {
  return axios.get<CategoryData>(`/category`);
}
