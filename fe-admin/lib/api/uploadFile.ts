import { AxiosError } from "axios";
import { uploadImage } from "./profile/uploadImage";
import { ApiError } from "../types";
import { toast } from "@/hooks/use-toast";

const uploadFile = async (file: File): Promise<string> => {
  const formData = new FormData();
  formData.append("file", file);
  const url = await uploadImage(formData)
    .then((imgRes) => {
      return imgRes.data.file;
    })
    .catch((err: AxiosError<ApiError>) => {
      toast({
        variant: "destructive",
        title: "Error",
        description: err.response?.data.message ?? "Create product failed",
      });
      return "";
    });
  return url;
};

export default uploadFile;
