import useSWR from "swr";

import { useFilteredList } from "./useFilterList";
import getAllProduct from "@/lib/api/product/getAllProduct";

export const useProductList = () => {
  const { filters, updateFilter, updateFilters, resetFilters, removeFilter } =
    useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    `product-${JSON.stringify(filters)}`,
    () => getAllProduct(filters)
  );

  return {
    filters,
    updateFilter,
    updateFilters,
    resetFilters,
    removeFilter,
    data,
    isLoading,
    error,
    mutate,
  };
};
