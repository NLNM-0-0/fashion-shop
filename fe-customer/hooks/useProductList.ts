import useSWR from "swr";

import getAllProduct from "@/lib/api/product/getAllProduct";
import { useProductFilter } from "@/components/product/product-filter-context";

export const useProductList = () => {
  const {
    filters,
    updateFilterUrl,
    updateFilters,
    resetFilters,
    removeFilter,
  } = useProductFilter();
  const { data, isLoading, error, mutate } = useSWR(
    `product-${JSON.stringify(filters)}`,
    () => getAllProduct(filters)
  );

  return {
    filters,
    updateFilterUrl,
    updateFilters,
    resetFilters,
    removeFilter,
    data,
    isLoading,
    error,
    mutate,
  };
};
