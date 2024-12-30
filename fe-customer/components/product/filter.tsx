"use client";

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "../ui/accordion";
import { z } from "zod";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import ColorPicker from "./color-picker";
import CheckboxGroup from "./checkbox-group";
import { Gender, Price, Season } from "@/lib/constants/enum";
import { priceToTitle } from "@/lib/constants";
import { FilterParams } from "@/hooks/useFilterList";
import { Button } from "../ui/button";
import { useCallback, useEffect } from "react";
import CategoryFilter from "./category-filter";

const FilterScheme = z.object({
  genders: z.array(z.string()),
  colors: z.array(z.string()),
  seasons: z.array(z.string()),
  price: z.string(),
});

export interface FilterProductProps {
  filters: FilterParams;
  updateFilters: (newFilters: FilterParams) => void;
  className?: string;
}
const Filter = ({ filters, updateFilters, className }: FilterProductProps) => {
  const { control, handleSubmit, reset } = useForm<
    z.infer<typeof FilterScheme>
  >({
    resolver: zodResolver(FilterScheme),
    defaultValues: {
      colors: Array.isArray(filters.colors)
        ? filters.colors
        : filters.colors
        ? [filters.colors]
        : [],
      genders: Array.isArray(filters.genders)
        ? filters.genders
        : filters.genders
        ? [filters.genders]
        : [],
      seasons: Array.isArray(filters.seasons)
        ? filters.seasons
        : filters.seasons
        ? [filters.seasons]
        : [],
      price: filters.price ? filters.price.toString() : "",
    },
  });

  const resetFilter = useCallback(() => {
    reset({
      colors: Array.isArray(filters.colors)
        ? filters.colors
        : filters.colors
        ? [filters.colors]
        : [],
      genders: Array.isArray(filters.genders)
        ? filters.genders
        : filters.genders
        ? [filters.genders]
        : [],
      seasons: Array.isArray(filters.seasons)
        ? filters.seasons
        : filters.seasons
        ? [filters.seasons]
        : [],
      price: filters.price ? filters.price.toString() : "",
    });
  }, [filters, reset]);

  const clearFilter = useCallback(() => {
    reset({
      colors: Array.isArray(filters.colors)
        ? filters.colors
        : filters.colors
        ? [filters.colors]
        : [],
      genders: Array.isArray(filters.genders)
        ? filters.genders
        : filters.genders
        ? [filters.genders]
        : [],
      seasons: Array.isArray(filters.seasons)
        ? filters.seasons
        : filters.seasons
        ? [filters.seasons]
        : [],
      price: filters.price ? filters.price.toString() : "",
    });
  }, [reset, filters]);

  const onSubmit: SubmitHandler<z.infer<typeof FilterScheme>> = (data) => {
    updateFilters(data);
  };

  useEffect(() => {
    resetFilter();
  }, [resetFilter]);

  return (
    <form className={className} onSubmit={handleSubmit(onSubmit)}>
      <div className="overflow-y-auto overflow-x-hidden px-1">
        <CategoryFilter />
        <Accordion type="multiple" className="w-full">
          <AccordionItem value="gender" className="border-b">
            <Controller
              control={control}
              name={`genders`}
              render={({ field }) => (
                <>
                  <AccordionTrigger className="capitalize">
                    Gender {field.value.length > 0 && `(${field.value.length})`}
                  </AccordionTrigger>
                  <AccordionContent className="pb-4 pt-2">
                    <CheckboxGroup
                      values={Object.values(Gender)}
                      selectedValue={field.value}
                      onValueChange={(item) => {
                        const selected = field.value.includes(item);
                        return selected
                          ? field.onChange(
                              field.value?.filter((value) => value !== item)
                            )
                          : field.onChange([...field.value, item]);
                      }}
                    />
                  </AccordionContent>
                </>
              )}
            />
          </AccordionItem>

          <AccordionItem value="color" className="border-b">
            <Controller
              control={control}
              name={`colors`}
              render={({ field }) => (
                <>
                  <AccordionTrigger className="capitalize">
                    Color {field.value.length > 0 && `(${field.value.length})`}
                  </AccordionTrigger>
                  <AccordionContent className="pb-4 pt-2">
                    <ColorPicker
                      value={field.value}
                      onValueChange={(item) => {
                        const selected = field.value.includes(item);
                        return selected
                          ? field.onChange(
                              field.value?.filter((value) => value !== item)
                            )
                          : field.onChange([...field.value, item]);
                      }}
                    />
                  </AccordionContent>
                </>
              )}
            />
          </AccordionItem>

          <AccordionItem value="season" className="border-b">
            <Controller
              control={control}
              name={`seasons`}
              render={({ field }) => (
                <>
                  <AccordionTrigger className="capitalize">
                    Season {field.value.length > 0 && `(${field.value.length})`}
                  </AccordionTrigger>
                  <AccordionContent className="pb-4 pt-2">
                    <CheckboxGroup
                      values={Object.values(Season)}
                      selectedValue={field.value}
                      onValueChange={(item) => {
                        const selected = field.value.includes(item);
                        return selected
                          ? field.onChange(
                              field.value?.filter((value) => value !== item)
                            )
                          : field.onChange([...field.value, item]);
                      }}
                    />
                  </AccordionContent>
                </>
              )}
            />
          </AccordionItem>

          <AccordionItem value="price" className="border-b">
            <Controller
              control={control}
              name={`price`}
              render={({ field }) => (
                <>
                  <AccordionTrigger className="capitalize">
                    Price {field.value.length > 0 && `(1)`}
                  </AccordionTrigger>
                  <AccordionContent className="pb-4 pt-2">
                    <CheckboxGroup
                      values={Object.values(Price)}
                      selectedValue={[field.value]}
                      titleValues={priceToTitle}
                      onValueChange={(item) => {
                        const selected = field.value === item;
                        return selected
                          ? field.onChange("")
                          : field.onChange(item);
                      }}
                    />
                  </AccordionContent>
                </>
              )}
            />
          </AccordionItem>
        </Accordion>
      </div>

      <div className="sticky bottom-0 py-4 flex gap-2">
        <Button
          className="rounded-full flex-1"
          variant={"outline"}
          type="button"
          onClick={clearFilter}
        >
          Clear
        </Button>
        <Button className="rounded-full flex-1">Apply</Button>
      </div>
    </form>
  );
};

export default Filter;
