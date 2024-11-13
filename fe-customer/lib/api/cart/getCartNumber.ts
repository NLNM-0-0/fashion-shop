import axios from "../axios";

export default function getCartNumber() {
  return axios.get(`/cart/number`);
}
