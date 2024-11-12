import axios from "../axios";

export const seeAllNotification = () => {
  return axios.post("/notification/seen");
};
