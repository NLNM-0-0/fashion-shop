import {
  Control,
  Controller,
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
}

const QuantityForm = ({ control, watch }: QuantityFormProps) => {
  const { fields, replace } = useFieldArray({
    control: control,
    name: "quantity",
  });
  const sizes = watch("sizes");
  const colors = watch("colors");

  useEffect(() => {
    const currentQuatities = sizes.flatMap((size) =>
      colors.map((color) => ({
        quantityKey: `${size.sizeName}-${color.colorName}`,
        size: size.sizeName,
        color: color.colorName,
        quantity:
          fields.find(
            (q) => (q.quantityKey = `${size.sizeName}-${color.colorName}`)
          )?.quantity || 0,
      }))
    );
    if (JSON.stringify(currentQuatities) !== JSON.stringify(fields)) {
      replace(currentQuatities);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sizes, colors]);

  return (
    <div className="flex flex-col gap-7">
      <ColorForm control={control} />
      <SizeForm control={control} />
      <div>
        <div className="font-medium mb-2">Quantity</div>
        <table className="min-w-full bg-white border">
          <thead>
            <tr>
              <th className="border px-4 py-2">Size \ Color</th>
              {colors.map((column) => (
                <th key={column.colorName} className="border px-4 py-2">
                  {column.colorName}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {sizes.map((size) => (
              <tr key={size.sizeName}>
                <td className="border px-4 py-2 font-semibold">
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
                      <td key={column.colorName} className="border px-4 py-2">
                        N/A
                      </td>
                    );

                  return (
                    <td key={column.colorName} className="border px-4 py-2">
                      <Controller
                        control={control}
                        name={`quantity.${index}.quantity`}
                        render={({ field }) => (
                          <Input type="number" {...field} />
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
  );
};

export default QuantityForm;
