import axios from "../axios";

export default async function sendEmailForgotPassword({
  email,
}: {
  email: string;
}) {
  const url = `/admin/auth/reset_password`;
  const data = {
    email: email,
  };

  const res = axios
    .post(url, data)
    .then((response) => {
      if (response) return response.data;
    })
    .catch((error) => {
      console.error("Error:", error);
      return error.response.data;
    });
  return res;
}
