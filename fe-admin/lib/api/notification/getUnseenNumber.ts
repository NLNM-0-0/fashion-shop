import axios from "../axios";

export default function getUnseenNumber() {
  return axios.get(`/admin/notification/number_unseen`);
}
