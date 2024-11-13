/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { Button } from "../ui/button";
import { PiPencilSimpleBold } from "react-icons/pi";
import { Input } from "../ui/input";
import { toast } from "@/hooks/use-toast";
import Image from "next/image";

export type ImageProps = {
  currentImage: string;
  handleImageSelected: () => void;
  image: any;
  setImage: (value: any) => void;
};

const ChangeImage = ({
  image,
  setImage,
  currentImage,
  handleImageSelected,
}: ImageProps) => {
  const [imagePreviews, setImagePreviews] = useState<string | null>(null);
  const handleMultipleImage = (event: any) => {
    const file = event.target.files[0];
    if (file) {
      if (file && file.type.includes("image")) {
        setImage(file);
        const reader = new FileReader();
        reader.onload = () => {
          if (reader.result) {
            setImagePreviews(reader.result as string);
          }
        };
        reader.readAsDataURL(file);
      } else {
        setImage(null);
        toast({
          variant: "destructive",
          title: "Có lỗi",
          description: "File không hợp lệ",
        });
      }
    } else {
      setImage(null);
    }
  };
  const [open, setOpen] = useState(false);
  const handleOpen = (value: boolean) => {
    if (value) {
      setImage(null);
      setImagePreviews(null);
    }
    setOpen(value);
  };
  return (
    <Dialog open={open} onOpenChange={handleOpen}>
      <DialogTrigger asChild>
        <Button variant={"ghost"} className="rounded-none w-full justify-start">
          <div className="flex gap-2 items-center text-primary">
            <PiPencilSimpleBold className="h-6 w-6 " />
            Change avatar
          </div>
        </Button>
      </DialogTrigger>
      <DialogContent className="max-w-[472px] p-0 bg-white">s
        <DialogHeader>
          <DialogTitle className="p-6 pb-0">Choose image</DialogTitle>
        </DialogHeader>
        <form>
          <div className="p-6 flex flex-col items-center gap-4 border-y-[1px]">
            <div className="relative rounded-full border overflow-clip">
              {image && imagePreviews ? (
                <Image
                  src={imagePreviews}
                  alt={`Preview`}
                  className="h-[100px] w-[100px] object-cover"
                  width={100}
                  height={100}
                />
              ) : (
                <Image
                  alt="avatar"
                  src={currentImage}
                  className="h-[100px] w-[100px] object-cover"
                  width={100}
                  height={100}
                />
              )}
            </div>

            <Input
              className="w-56"
              id="img"
              type="file"
              onChange={handleMultipleImage}
            ></Input>
          </div>
          <div className="p-4 flex-1 flex justify-end">
            <div className="flex gap-4">
              <Button
                type="button"
                variant={"outline"}
                onClick={() => setOpen(false)}
              >
                Cancel
              </Button>

              <Button
                type="button"
                onClick={() => {
                  handleImageSelected();
                  setOpen(false);
                }}
              >
                Change
              </Button>
            </div>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ChangeImage;
