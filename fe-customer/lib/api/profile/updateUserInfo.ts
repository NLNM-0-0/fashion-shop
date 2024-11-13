import axios from "../axios";

interface UpdateAvatarPayload {
  image?: string;
  name?: string;
  dob?: string;
  address?: string;
  male?:boolean
}
export const updateUserInfo = (payload: UpdateAvatarPayload) => {
  return axios.put(`/user/info`, payload);
};
