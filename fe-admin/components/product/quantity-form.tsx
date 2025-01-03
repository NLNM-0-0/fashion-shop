import {
  Control,
  Controller,
  FieldErrors,
  useFieldArray,
  UseFormWatch,
} from "react-hook-form";
import { ProductSchema } from "./add-new-product";
import { z } from "zod";
import ColorForm from "./color-form";
import SizeForm from "./size-form";
import { useEffect } from "react";
import { Input } from "../ui/input";
export interface QuantityFormProps {
  control: Control<z.infer<typeof ProductSchema>>;
  watch: UseFormWatch<z.infer<typeof ProductSchema>>;
  errors: FieldErrors<z.infer<typeof ProductSchema>>;
}

const QuantityForm = ({ control, watch, errors }: QuantityFormProps) => {
  const { fields, replace } = useFieldArray({
    control: control,
    name: "quantity",
  });
  const sizes = watch("sizes");
  const colors = watch("colors");

  useEffect(() => {
    if (sizes && colors) {
      const currentQuatities = sizes.flatMap((size) =>
        colors.map((color) => {
          return {
            quantityKey: `${size.sizeName}-${color.colorName}`,
            size: size.sizeName,
            color: color.colorName,
            quantity:
              fields.find(
                (q) => q.quantityKey === `${size.sizeName}-${color.colorName}`
              )?.quantity || 0,
          };
        })
      );
      if (JSON.stringify(currentQuatities) !== JSON.stringify(fields)) {
        replace(currentQuatities);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sizes, colors]);

  return (
    <div className="flex flex-col gap-7">
      <div>
        <ColorForm control={control} />
        {errors.colors && (
          <span className="error___message">{errors.colors.message}</span>
        )}
      </div>
      <div>
        <SizeForm control={control} />
        {errors.sizes && (
          <span className="error___message">{errors.sizes.message}</span>
        )}
      </div>
      <div>
        <div className="font-medium mb-2">Quantity</div>
        <div className="min-w-full overflow-x-auto max-w-[50vw]">
          <table className="bg-white">
            <thead>
              <tr>
                <th className="border xl:px-3 px-2 py-2 xl:text-base text-sm">
                  Size \ Color
                </th>
                {colors &&
                  colors.map((column) => (
                    <th
                      key={column.colorName}
                      className="border xl:px-3 px-2 py-2 xl:text-base text-sm min-w-20"
                    >
                      {column.colorName}
                    </th>
                  ))}
              </tr>
            </thead>
            <tbody>
              {sizes &&
                sizes.map((size) => (
                  <tr key={size.sizeName}>
                    <td className="border xl:px-3 px-2 py-2 font-semibold">
                      {size.sizeName}
                    </td>
                    {colors.map((column) => {
                      const index = fields.findIndex(
                        (quantity) =>
                          quantity.quantityKey ===
                          `${size.sizeName}-${column.colorName}`
                      );
                      if (index === -1)
                        return (
                          <td
                            key={`${size.sizeName}-${column.colorName}`}
                            className="border xl:px-3 px-2 py-2"
                          >
                            N/A
                          </td>
                        );

                      return (
                        <td
                          key={`${size.sizeName}-${column.colorName}`}
                          className="border xl:px-3 px-2 py-2"
                        >
                          <Controller
                            key={`${size.sizeName}-${column.colorName}`}
                            control={control}
                            name={`quantity.${index}.quantity`}
                            render={({ field }) => (
                              <Input type="number" {...field} className="px-2"/>
                            )}
                          />
                        </td>
                      );
                    })}
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default QuantityForm;
