import axios from "../axios";

export const likeItem = (id: number) => {
  return axios.post(`/like/like/${id}`);
};

export const unlikeItem = (id: number) => {
  return axios.delete(`like/unlike/${id}`);
};
