import axios from "../axios";

export const seeNotification = (id: number) => {
  return axios.post(`/notification/${id}/seen`);
};
