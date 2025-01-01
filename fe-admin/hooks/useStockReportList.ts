import useSWR from "swr";
import { useFilteredList } from "./useFilterList";
import getStockReport from "@/lib/api/report/getStockReport";
import { UserInfo } from "@/lib/types";

export const useStockReportList = (user: UserInfo | null) => {
  const {
    filters,
    filtersReady,
    updateFilter,
    updateFilters,
    resetFilters,
    removeFilter,
  } = useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    filtersReady.current && user?.admin
      ? `stockReport-${JSON.stringify(filters)}`
      : null,
    () => getStockReport(filters),
    {
      revalidateOnFocus: false,
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
