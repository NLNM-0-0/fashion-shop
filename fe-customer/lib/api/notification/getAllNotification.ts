import { NotificationData, NotiFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getAllNotification(
  params: NotiFilterParam
): Promise<AxiosResponse<NotificationData>> {
  return axios.get<NotificationData>(
    `/notification?${encodeParams(params)}`
  );
}
