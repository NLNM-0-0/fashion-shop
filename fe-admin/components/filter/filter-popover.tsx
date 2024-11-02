import { FormFilterValues } from "@/lib/types";
import {
  Controller,
  SubmitHandler,
  useFieldArray,
  useForm,
} from "react-hook-form";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";
import { Button } from "../ui/button";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { AiOutlineClose } from "react-icons/ai";
import GenderRadioButton from "../ui/gender-radio-button";
import { Input } from "../ui/input";
import { FilterInputType } from "@/lib/constants/enum";
import { LuFilter } from "react-icons/lu";
import { useEffect } from "react";
import { FilterPopoverProps } from "./filter";

const FilterPopover = ({
  title,
  filters,
  filterValues,
  onApplyFilters,
  open,
  onOpenChange,
}: FilterPopoverProps) => {
  const { register, reset, control, handleSubmit } = useForm<FormFilterValues>({
    defaultValues: {
      filters: Object.entries(filters).map(([key, value]) => ({
        type: key,
        value,
      })),
    },
  });
  const { fields, append, remove } = useFieldArray({
    control: control,
    name: "filters",
  });

  const onSubmit: SubmitHandler<FormFilterValues> = (data) => {
    onApplyFilters(data);
    onOpenChange(false);
  };

  useEffect(() => {
    reset({
      filters: Object.entries(filters).map(([key, value]) => ({
        type: key,
        value,
      })),
    });
  }, [filters, reset]);

  return (
    <Popover
      open={open}
      onOpenChange={(open) => {
        onOpenChange(open);
      }}
    >
      <PopoverTrigger asChild>
        <Button variant="outline" className="lg:px-3 px-2">
          Filter
          <LuFilter className="ml-1 h-4 w-4" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-96 mx-10">
        <form className="flex flex-col gap-4" onSubmit={handleSubmit(onSubmit)}>
          <div className="space-y-2">
            <p className="text-base text-muted-foreground">{title}</p>
          </div>
          <div className="flex flex-col gap-4">
            {fields.map((item, index) => {
              const filterItem = filterValues.find((v) => v.type === item.type);
              return (
                <div className="flex gap-2 items-center" key={item.id}>
                  <span className="basis-1/4 font-medium">
                    {filterItem?.title}
                  </span>
                  {filterItem?.inputType === FilterInputType.TEXT ? (
                    <Input
                      {...register(`filters.${index}.value`)}
                      className="flex-1"
                      type="text"
                    ></Input>
                  ) : filterItem?.inputType === FilterInputType.NUMBER ? (
                    <Input
                      {...register(`filters.${index}.value`)}
                      className="flex-1"
                      type="number"
                      min={1901}
                      max={2024}
                      title="Invalid year"
                    ></Input>
                  ) : filterItem?.inputType === FilterInputType.YEAR ? (
                    <Input
                      {...register(`filters.${index}.value`)}
                      className="flex-1"
                      type="number"
                    ></Input>
                  ) : filterItem?.inputType === FilterInputType.MONTH ? (
                    <Input
                      {...register(`filters.${index}.value`)}
                      className="flex-1"
                      type="number"
                      min={1}
                      max={12}
                    ></Input>
                  ) : filterItem?.inputType === FilterInputType.GENDER ? (
                    <Controller
                      control={control}
                      name={`filters.${index}.value`}
                      render={({ field }) => (
                        <div className="flex flex-1 gap-2">
                          <GenderRadioButton
                            title="Male"
                            value={true}
                            onSelect={(value) =>
                              field.onChange(value.toString())
                            }
                            selected={field.value === "true"}
                            className="flex-1"
                          />
                          <GenderRadioButton
                            title="Female"
                            value={false}
                            onSelect={(value) =>
                              field.onChange(value.toString())
                            }
                            selected={field.value === "false"}
                            className="flex-1"
                          />
                        </div>
                      )}
                    />
                  ) : null}
                  <Button
                    type="button"
                    variant={"ghost"}
                    className={`px-3 `}
                    onClick={() => {
                      remove(index);
                    }}
                  >
                    <AiOutlineClose />
                  </Button>
                </div>
              );
            })}
          </div>
          {fields.length === filterValues.length ? null : (
            <div className="flex justify-center">
              <Select
                value=""
                onValueChange={(value) => {
                  append({ type: value, value: "" });
                }}
              >
                <SelectTrigger className="w-[210px] flex justify-between ml-12 px-3">
                  <SelectValue placeholder="Choose filter" />
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    {filterValues.map((item) => {
                      return fields.findIndex((v) => v.type === item.type) ===
                        -1 ? (
                        <SelectItem key={item.type} value={item.type}>
                          {item.title}
                        </SelectItem>
                      ) : null;
                    })}
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>
          )}
          <Button type="submit" className="self-end">
            Filter
          </Button>
        </form>
      </PopoverContent>
    </Popover>
  );
};

export default FilterPopover;
