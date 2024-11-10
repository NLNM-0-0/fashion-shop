import Image from "next/image";
import React, { useState, ChangeEvent, useRef } from "react";
import { Button } from "./button";
import { PiPlus } from "react-icons/pi";

interface ImageUploadProps {
  onUpload?: (files: File[]) => void;
}

const ImageUpload: React.FC<ImageUploadProps> = ({ onUpload }) => {
  const [fileList, setFileList] = useState<File[]>([]);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    setFileList((prevList) => [...prevList, ...files]);

    if (onUpload) onUpload([...fileList, ...files]);
  };

  const handleButtonClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const handleRemove = (index: number) => {
    const updatedFileList = fileList.filter((_, i) => i !== index);
    setFileList(updatedFileList);
  };

  return (
    <div>
      <input
        type="file"
        accept="image/*"
        multiple
        onChange={handleFileChange}
        ref={fileInputRef}
        style={{ display: "none" }}
      />

      <div className="flex flex-wrap gap-3">
        {fileList.map((file, index) => (
          <div key={index} className="relative">
            <div className="h-20 w-20 rounded-sm border-solid border-fs-gray-lighter border p-1">
              <Image
                width={100}
                height={100}
                src={URL.createObjectURL(file)}
                alt={`Preview ${index}`}
                className="w-full h-full object-cover"
                onLoad={() => URL.revokeObjectURL(URL.createObjectURL(file))}
              />
            </div>
            <Button
              type="button"
              onClick={() => handleRemove(index)}
              variant={"outline"}
              className="absolute -top-2 -right-2 rounded-full h-5 w-5 p-0"
            >
              <div className="h-[2px] w-2.5 bg-fs-error rounded-lg"></div>
            </Button>
          </div>
        ))}
        <Button
          type="button"
          onClick={handleButtonClick}
          variant={"outline"}
          className="h-20 w-20 rounded-s p-0"
        >
          <PiPlus
            style={{ height: 30, width: 30 }}
            className="text-fs-gray-darker"
          />
        </Button>
      </div>
    </div>
  );
};

export default ImageUpload;
