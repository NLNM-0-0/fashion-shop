import { Color } from "@/lib/constants/enum";
import { ProductColor } from "@/lib/types";
import { cn } from "@/lib/utils";
import { LuCheck } from "react-icons/lu";

export interface ColorItemProps {
  color: ProductColor;
  selected: boolean;
  disable?: boolean;
  onSelected: (value: string) => void;
}

const ColorItem = ({
  color,
  selected,
  disable,
  onSelected,
}: ColorItemProps) => {
  return (
    <div
      className={cn(
        "flex flex-col gap-1 items-center cursor-pointer hover:text-fs-gray-dark",
        disable && "pointer-events-none"
      )}
      onClick={() => onSelected(color.name)}
    >
      <div
        className={cn(
          "h-7 w-7 rounded-full flex justify-center items-center relative",
          color.name === Color.WHITE && "border",
          color.name === Color.MULTI_COLOR && "multicolor__picker"
        )}
        style={{ background: color.hex }}
      >
        {selected && !disable && (
          <LuCheck
            size={18}
            style={{ strokeWidth: 3 }}
            className={cn(
              color.name === Color.WHITE ? "text-black" : "text-white"
            )}
          />
        )}
        {disable && (
          <div className="absolute h-0.5 w-9 bg-fs-black rounded-xl -rotate-45" />
        )}
      </div>
      <span className="capitalize text-sm">
        {color.name === Color.MULTI_COLOR
          ? "Multi Color"
          : color.name.toLowerCase()}
      </span>
    </div>
  );
};

export default ColorItem;
