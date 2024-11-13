import { colorToClassMap } from "@/lib/constants";
import { Color } from "@/lib/constants/enum";
import { cn } from "@/lib/utils";
import { LuCheck } from "react-icons/lu";

export interface ColorItemProps {
  color: Color;
  selected: boolean;
  onSelected: (value: Color) => void;
}

const ColorItem = ({ color, selected, onSelected }: ColorItemProps) => {
  const bgColor = colorToClassMap[color];
  return (
    <div
      className="flex flex-col gap-1 items-center cursor-pointer hover:text-fs-gray-dark"
      onClick={() => onSelected(color)}
    >
      <div
        className={cn(
          "h-7 w-7 rounded-full flex justify-center items-center",
          color === Color.WHITE && "border"
        )}
        style={{ background: bgColor }}
      >
        {selected && (
          <LuCheck
            className={cn(color === Color.WHITE ? "text-black" : "text-white")}
          />
        )}
      </div>
      <span className="capitalize text-sm">{color.toLowerCase()}</span>
    </div>
  );
};

export default ColorItem;
