import { Control, useFieldArray } from "react-hook-form";
import { ProductSchema } from "./add-new-product";
import { z } from "zod";
import ColorPicker from "./color-picker";
import { Color } from "@/lib/constants/enum";
export interface ColorFormProps {
  control: Control<z.infer<typeof ProductSchema>>;
}

const ColorForm = ({ control }: ColorFormProps) => {
  const { fields, append, remove } = useFieldArray({
    control: control,
    name: "colors",
  });

  const handleColorSelect = (colorName: string) => {
    const selectedIndex = fields.findIndex(
      (color) => color.colorName === colorName
    );

    if (selectedIndex > -1) remove(selectedIndex);
    else append({ colorName: colorName });
  };

  return (
    <div>
      <div className="font-medium  text-black mb-3">
        Color <span className="error___message">*</span>
      </div>
      <ColorPicker
        value={fields.map((item) => item.colorName as Color)}
        onValueChange={handleColorSelect}
      />
    </div>
  );
};

export default ColorForm;
