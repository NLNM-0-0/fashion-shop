import useSWR from "swr";
import { useFilteredList } from "./useFilterList";
import getSaleReport from "@/lib/api/report/getSaleReport";
import { UserInfo } from "@/lib/types";

export const useSaleReportList = (user: UserInfo | null) => {
  const {
    filters,
    filtersReady,
    updateFilter,
    updateFilters,
    resetFilters,
    removeFilter,
  } = useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    user?.admin === true && filtersReady.current
      ? `saleReport-${JSON.stringify(filters)}`
      : null,
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
