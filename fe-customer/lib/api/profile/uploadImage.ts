import axios from "../axios";

interface FileResponse {
  file: string;
}

export const uploadImage = (formData: FormData) => {
  return axios.post<FileResponse>(`/file`, formData);
};
