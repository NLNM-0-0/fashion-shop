/* eslint-disable @typescript-eslint/no-explicit-any */
import { toast } from "@/hooks/use-toast";
import { updateUserInfo } from "@/lib/api/profile/updateUserInfo";
import { uploadImage } from "@/lib/api/profile/uploadImage";
import { useState } from "react";
import ChangeImage from "./change-image";
import { AxiosError } from "axios";
import { ApiError } from "next/dist/server/api-utils";

interface ChangeAvatarProps {
  onChanged: () => void;
  currentImage: string;
}
const ChangeAvatar = ({ currentImage, onChanged }: ChangeAvatarProps) => {
  const [image, setImage] = useState<any>();
  const handleImageSelected = async () => {
    if (!image) {
      return;
    }
    const formData = new FormData();
    formData.append("file", image);
    uploadImage(formData)
      .then((imgRes) => {
        updateUserInfo({ image: imgRes.data.file })
          .then(() => {
            onChanged();
            toast({
              variant: "success",
              title: "Success",
              description: "Update avatar successfully",
            });
          })
          .catch((err: AxiosError<ApiError>) => {
            toast({
              variant: "destructive",
              title: "Error",
              description: err.response?.data.message ?? "Update avatar failed",
            });
          });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Upload avatar failed",
        });
      });
  };
  return (
    <ChangeImage
      image={image}
      setImage={setImage}
      handleImageSelected={handleImageSelected}
      currentImage={currentImage}
    />
  );
};

export default ChangeAvatar;
