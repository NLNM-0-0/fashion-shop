import axios from "../axios";

export const seeAllNotification = () => {
  return axios.post("/admin/notification/seen");
};
