import { Control, useFieldArray } from "react-hook-form";
import { ProductSchema } from "./add-new-product";
import { z } from "zod";
import { Input } from "../ui/input";
import { useState } from "react";
import { Button } from "../ui/button";
import SizeRadioButton from "../ui/size-button";
export interface SizeFormProps {
  control: Control<z.infer<typeof ProductSchema>>;
}

const SizeForm = ({ control }: SizeFormProps) => {
  const { fields, append, remove } = useFieldArray({
    control: control,
    name: "sizes",
  });

  const [sizeString, setSizeString] = useState("");

  const handleSizeSelect = () => {
    const sizeArray = Array.from(
      new Set(
        sizeString
          .split(",")
          .map((size) => size.trim())
          .filter((size) => size)
      )
    );

    sizeArray.forEach((size) => {
      const exists = fields.some((field) => field.sizeName === size);
      if (!exists) append({ sizeName: size });
    });

    setSizeString("");
  };

  return (
    <div className="flex flex-col gap-3">
      <div className="font-medium  text-black">
        Size
        <span className="text-fs-gray-dark text-sm font-normal">
          {" "}
          (e.g., S, M, L, XL) <span className="error___message">*</span>
        </span>
      </div>
      <div className="flex gap-2">
        <Input
          type="text"
          placeholder="Enter size name"
          value={sizeString}
          onChange={(e) => setSizeString(e.target.value)}
        ></Input>
        <Button variant="outline" type="button" onClick={handleSizeSelect}>
          Add
        </Button>
      </div>
      <div className="flex gap-2 flex-wrap">
        {fields.map((item, index) => (
          <div key={`size-${index}`} className="relative">
            <SizeRadioButton selected={false} readonly name={item.sizeName} />
            <Button
              type="button"
              onClick={() => remove(index)}
              variant={"outline"}
              className="absolute -top-2 -right-2 rounded-full h-5 w-5 p-0"
            >
              <div className="h-[2px] w-2.5 bg-fs-error rounded-lg"></div>
            </Button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SizeForm;
