import React, { useState } from "react";
import Filter, { FilterProductProps } from "./filter";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Button } from "../ui/button";
import { PiSlidersHorizontal } from "react-icons/pi";
import { FilterParams } from "@/hooks/useFilterList";

const FilterSheet = ({ filters, updateFilters }: FilterProductProps) => {
  const [open, setOpen] = useState(false);

  const updateFiltersAndCloseSheet = (newFilters: FilterParams) => {
    updateFilters(newFilters);
    setOpen(false);
  };
  return (
    <>
      <Sheet open={open} onOpenChange={setOpen}>
        <SheetTrigger asChild className="lg:hidden block">
          <Button variant="outline" className="rounded-full flex gap-1">
            Show Filter <PiSlidersHorizontal className="!w-6 !h-6" />
          </Button>
        </SheetTrigger>
        <SheetContent
          className="lg:hidden block"
          overlayClassName="lg:hidden block"
        >
          <SheetHeader className="pb-4">
            <SheetTitle>Filter Products</SheetTitle>
          </SheetHeader>
          <Filter
            filters={filters}
            updateFilters={updateFiltersAndCloseSheet}
            className="h-full"
          />
        </SheetContent>
      </Sheet>
    </>
  );
};

export default FilterSheet;
