import useSWR from "swr";
import getAllCustomer from "../lib/api/customer/getAllCustomer";
import { useFilteredList } from "./useFilterList";

export const useCustomerList = () => {
  const { filters, updateFilter, resetFilters, removeFilter } =
    useFilteredList();
  const { data, isLoading, error, mutate } = useSWR(
    `customer-${JSON.stringify(filters)}`,
    () => getAllCustomer(filters)
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
