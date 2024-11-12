import axios from "../axios";

export default function getUnseenNumber() {
  return axios.get(`/notification/number_unseen`);
}
