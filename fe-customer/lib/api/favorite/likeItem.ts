import axios from "../axios";

export const likeItem = (id: number) => {
  return axios.post(`/like/like/${id}`);
};
