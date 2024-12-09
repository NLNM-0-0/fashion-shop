import { Color } from "@/lib/constants/enum";
import ColorItem from "./color-item";
import React from "react";
import { colorToClassMap } from "@/lib/constants";

export interface ColorPickerProps {
  value: string[];
  onValueChange: (value: string) => void;
}
const ColorPicker = ({ value, onValueChange }: ColorPickerProps) => {
  const colorList = Object.values(Color);

  return (
    <div className="grid grid-cols-3 gap-y-5">
      {colorList.map((item) => (
        <ColorItem
          key={item}
          color={{ name: item, hex: colorToClassMap[item] }}
          selected={value.map((color) => color).includes(item)}
          onSelected={onValueChange}
        />
      ))}
    </div>
  );
};

export default ColorPicker;
