import { FavoriteData } from "@/lib/types";
import axios from "../axios";
import { AxiosResponse } from "axios";

export default function getAllFavorite(): Promise<AxiosResponse<FavoriteData>> {
  return axios.get<FavoriteData>(`/like`);
}
