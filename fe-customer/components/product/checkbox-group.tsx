import React from "react";
import { Checkbox } from "../ui/checkbox";

export interface CheckboxGroupProps {
  values: string[];
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  titleValues?: Record<any, string>;
  selectedValue: string[];
  onValueChange: (value: string) => void;
}
const CheckboxGroup = ({
  values,
  selectedValue,
  titleValues,
  onValueChange,
}: CheckboxGroupProps) => {
  return (
    <div className="flex flex-col gap-2">
      {values.map((item) => (
        <div key={item} className="flex items-center gap-2">
          <Checkbox
            id={item}
            checked={selectedValue.includes(item)}
            onCheckedChange={() => {
              onValueChange(item);
            }}
          />
          <label htmlFor={item} className="capitalize text-base">
            {titleValues ? titleValues[item] : item.toLowerCase()}
          </label>
        </div>
      ))}
    </div>
  );
};

export default CheckboxGroup;
