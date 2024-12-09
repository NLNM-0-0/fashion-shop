"use client";
import { useState } from "react";
import Filter from "./filter";
import FilterBySelect from "./filter-by-select";
import { useProductList } from "@/hooks/useProductList";
import { SortType } from "@/lib/constants/enum";

const AllProductLayout = () => {
  const { filters, updateFilterUrl } = useProductList();
  const [selectedSortType, setSelectedSortType] = useState<string | null>(null);

  const onSelectedSortTypeChange = (sortType: string | null) => {
    setSelectedSortType(sortType);
    switch (sortType) {
      case SortType.Newest:
        updateFilterUrl({ sortNew: "true", sortPrice: null });
        break;
      case SortType.Oldest:
        updateFilterUrl({ sortNew: "false", sortPrice: null });
        break;
      case SortType.PriceHighLow:
        updateFilterUrl({ sortNew: null, sortPrice: "true" });
        break;
      case SortType.PriceLowHigh:
        updateFilterUrl({ sortNew: null, sortPrice: "false" });
        break;
    }
  };

  return (
    <div className="flex flex-row overflow-hidden gap-10">
      <div className="w-[260px]">
        <Filter filters={filters} updateFilters={updateFilterUrl} />
      </div>
      <div className="flex-1">
        <FilterBySelect
          selectedValue={selectedSortType}
          setSelectedValue={onSelectedSortTypeChange}
        />
      </div>
    </div>
  );
};

export default AllProductLayout;
