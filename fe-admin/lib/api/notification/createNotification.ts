import { CreateNotificationPayload } from "@/lib/types";
import axios from "../axios";

export const createNotification = (payload: CreateNotificationPayload) => {
  return axios.post("/admin/notification", payload);
};
