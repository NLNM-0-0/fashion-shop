import axios from "../axios";

export const seeNotification = (id: number) => {
  return axios.post(`/admin/notification/${id}/seen`);
};
