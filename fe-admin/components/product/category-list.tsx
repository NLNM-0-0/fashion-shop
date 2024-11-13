"use client";

import React, { useEffect, useState } from "react";
import { LuCheck, LuChevronsUpDown } from "react-icons/lu";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";
import { Button } from "../ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "../ui/command";
import { cn } from "@/lib/utils";
import { Skeleton } from "../ui/skeleton";
import { Category } from "@/lib/types";
import { useCategoryList } from "@/hooks/useCategory";

export interface CategoryListProps {
  category: string | number;
  setCategory: (category: string | number, name?: string) => void;
  isId: boolean;
  readonly?: boolean;
}
const CategoryList = ({
  isId,
  category,
  setCategory,
  readonly,
}: CategoryListProps) => {
  const [open, setOpen] = useState(false);
  const { data, error, isLoading } = useCategoryList();
  const [categoryList, setCategoryList] = useState<Array<Category>>([]);
  useEffect(() => {
    if (data) {
      setCategoryList(data.data.data);
    }
  }, [data]);

  if (error) return <div>Failed to load</div>;
  if (isLoading || !data) {
    return <Skeleton className="h-10 w-full" />;
  } else {
    return (
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button
            disabled={readonly}
            variant="outline"
            role="combobox"
            aria-expanded={open}
            className="justify-between w-full min-w-0 h-10"
          >
            {category && category !== -1
              ? categoryList.find(
                  (item) =>
                    (isId && item.id.toString() === category.toString()) ||
                    (!isId && item.name === category)
                )?.name
              : "Choose category"}
            <LuChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="PopoverContent rounded-xl w-full">
          <Command className="w-full">
            <CommandInput placeholder="Find category's name" />
            <CommandEmpty className="py-2 px-6">
              <div className="text-sm">Not found</div>
            </CommandEmpty>
            <CommandList>
              <CommandGroup>
                {categoryList.map((item) => (
                  <CommandItem
                    value={item.name}
                    key={item.id}
                    onSelect={() => {
                      if (isId) {
                        setCategory(item.id, item.name);
                      } else {
                        setCategory(item.name, item.name);
                      }

                      setOpen(false);
                    }}
                  >
                    <LuCheck
                      className={cn(
                        "mr-2 h-4 w-4",
                        isId
                          ? item.id.toString() === category.toString()
                            ? "opacity-100"
                            : "opacity-0"
                          : item.name === category
                          ? "opacity-100"
                          : "opacity-0"
                      )}
                    />
                    {item.name}
                  </CommandItem>
                ))}
              </CommandGroup>
            </CommandList>
          </Command>
        </PopoverContent>
      </Popover>
    );
  }
};

export default CategoryList;
