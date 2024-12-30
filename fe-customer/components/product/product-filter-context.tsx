"use client";
import { FilterParams, useFilteredList } from "@/hooks/useFilterList";
import { createContext, useContext, useMemo } from "react";

interface ProductFilterType {
  filters: FilterParams;
  updateFilters: (newFilters: FilterParams) => void;
  updateFilterUrl: (newFilters: FilterParams) => void;
  resetFilters: () => void;
  removeFilter: (filterName: string) => void;
}

const ProductFilterContext = createContext<ProductFilterType | undefined>(
  undefined
);

export const ProductFilterProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const {
    filters,
    updateFilterUrl,
    updateFilters,
    resetFilters,
    removeFilter,
  } = useFilteredList();

  const value = useMemo(
    () => ({
      filters,
      updateFilterUrl,
      updateFilters,
      resetFilters,
      removeFilter,
    }),
    [filters, updateFilters, updateFilterUrl, , resetFilters, removeFilter]
  );

  return (
    <ProductFilterContext.Provider value={value}>
      {children}
    </ProductFilterContext.Provider>
  );
};

export const useProductFilter = (): ProductFilterType => {
  const context = useContext(ProductFilterContext);
  if (!context) {
    throw new Error("useProductFilter must be used within a FilterProvider");
  }
  return context;
};
