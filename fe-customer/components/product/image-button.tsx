import Image from "next/image";
import React from "react";

interface ImageButtonProps {
  image: string;
  selected: boolean;
  onClick: () => void;
}
const ImageButton = ({ image, selected, onClick }: ImageButtonProps) => {
  return (
    <div
      className="relative rounded-sm w-fit overflow-clip cursor-pointer"
      onClick={onClick}
    >
      <Image
        src={image}
        className="object-cover h-[70px] w-[70px]"
        alt="img"
        width={70}
        height={70}
      />
      {selected && (
        <div className="absolute top-0 right-0 bottom-0 left-0 bg-fs-gray-darker opacity-40"></div>
      )}
    </div>
  );
};

export default ImageButton;
