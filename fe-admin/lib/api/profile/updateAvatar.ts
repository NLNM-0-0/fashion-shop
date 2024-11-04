import axios from "../axios";

interface UpdateAvatarPayload {
  image: string;
}
export const updateAvatar = (payload: UpdateAvatarPayload) => {
  return axios.put(`/admin/user/info`, payload);
};
