import useSWR from "swr";
import { useFilteredList } from "./useFilterList";
import getAllStaff from "../lib/api/staff/getAllStaff";

export const useStaffList = () => {
  const { filters, filtersReady, updateFilter, resetFilters, removeFilter } =
    useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    filtersReady.current ? filters : null,
    () => getAllStaff(filters),
    {
      revalidateOnFocus: false,
    }
  );

  return {
    filters,
    updateFilter,
    resetFilters,
    removeFilter,
    data,
    isLoading,
    error,
    mutate,
  };
};
