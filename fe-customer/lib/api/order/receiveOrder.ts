import axios from "../axios";

export const receiveOrder = (id: string) => {
  return axios.put(`/order/${id}`);
};
