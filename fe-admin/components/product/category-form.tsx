import { Control, useFieldArray } from "react-hook-form";
import { z } from "zod";
import { ProductSchema } from "./add-new-product";
import CategoryList from "./category-list";
import { useState } from "react";
import SizeRadioButton from "../ui/size-button";
import { Button } from "../ui/button";

export interface CategoryFormProps {
  control: Control<z.infer<typeof ProductSchema>>;
}
const CategoryForm = ({ control }: CategoryFormProps) => {
  const [category] = useState(-1);

  const { fields, append, remove } = useFieldArray({
    control: control,
    name: "categories",
  });

  const onSelect = (categoryId: number, name: string | undefined) => {
    const selectedIndex = fields.findIndex(
      (item) => item.categoryId === categoryId
    );
    if (selectedIndex > -1) {
      return;
    } else {
      append({ categoryId: categoryId, categoryName: name ?? "" });
    }
  };

  return (
    <div className="flex flex-col gap-3">
      <CategoryList
        isId
        category={category}
        setCategory={(value: string | number, name) => onSelect(+value, name)}
      />
      <div className="flex flex-wrap gap-3">
        {fields.map((item, index) => (
          <div key={`category-${index}`} className="relative w-fit">
            <SizeRadioButton
              selected={false}
              readonly
              name={item.categoryName}
            />
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

export default CategoryForm;
