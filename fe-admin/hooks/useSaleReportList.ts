import useSWR from "swr";
import { useFilteredList } from "./useFilterList";
import getSaleReport from "@/lib/api/report/getSaleReport";

export const useSaleReportList = () => {
  const {
    filters,
    filtersReady,
    updateFilter,
    updateFilters,
    resetFilters,
    removeFilter,
  } = useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    filtersReady.current ? `saleReport-${JSON.stringify(filters)}` : null,
    () => getSaleReport(filters),
    {
      revalidateOnFocus: false,
      shouldRetryOnError: false,
    }
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
