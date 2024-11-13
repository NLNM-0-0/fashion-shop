import { Color } from "@/lib/constants/enum";
import ColorItem from "./color-item";

export interface ColorPickerProps {
  value: Color[];
  onValueChange: (value: Color) => void;
}
const ColorPicker = ({ value, onValueChange }: ColorPickerProps) => {
  const colorList = Object.values(Color);

  return (
    <div className="flex gap-x-10 gap-y-5 flex-wrap">
      {colorList.map((item) => (
        <ColorItem
          key={item}
          color={item}
          selected={value.includes(item)}
          onSelected={onValueChange}
        />
      ))}
    </div>
  );
};

export default ColorPicker;
