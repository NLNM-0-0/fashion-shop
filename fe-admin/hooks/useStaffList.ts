import useSWR from "swr";
import { useFilteredList } from "./useFilterList";
import getAllStaff from "../lib/api/staff/getAllStaff";
import { UserInfo } from "@/lib/types";

export const useStaffList = (user: UserInfo | null) => {
  const { filters, filtersReady, updateFilter, resetFilters, removeFilter } =
    useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    filtersReady.current && user?.admin
      ? `staff-${JSON.stringify(filters)}`
      : null,
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
