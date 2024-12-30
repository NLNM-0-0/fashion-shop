import useSWR from "swr";
import { useFilteredList } from "./useFilterList";
import getAllNotification from "@/lib/api/notification/getAllNotification";

export const useNotificationList = () => {
  const { filters, updateFilter, resetFilters, removeFilter } =
    useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    `notification-${JSON.stringify(filters)}`,
    () => getAllNotification(filters),
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
