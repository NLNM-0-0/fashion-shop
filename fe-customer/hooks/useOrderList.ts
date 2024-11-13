import { useFilteredList } from "./useFilterList";
import getAllOrder from "@/lib/api/order/getAllOrder";
import { useCallback, useRef } from "react";
import useSWRInfinite from "swr/infinite";

export const useOrderList = () => {
  const { filters, updateFilter, updateFilters, resetFilters, removeFilter } =
    useFilteredList();

  const fetcher = (page: string) => {
    return getAllOrder(filters, +page + 1);
  };
  const { data, error, isValidating, setSize, size } = useSWRInfinite(
    (index) => `orders-${JSON.stringify(filters)}-${index}`, // Include filters in the key
    (key) => fetcher(key.split("-").pop() ?? "1"),
    {
      revalidateFirstPage: false, // Only fetch first page initially
      persistSize: true,
    }
  );
  const orders = data ? data.flatMap((page) => page.data.data) : [];
  const isLoadingInitialData = !data && !error;
  const isLoadingMore =
    isLoadingInitialData ||
    (isValidating && size > 0 && data && typeof data[size - 1] === "undefined");
  const isReachingEnd =
    data &&
    data[data.length - 1]?.data.page.index ===
      data[data.length - 1]?.data.page.totalPages;

  const observer = useRef<IntersectionObserver | null>(null);
  const lastItemRef = useCallback(
    (node: HTMLDivElement) => {
      if (isLoadingMore) return;
      if (observer.current) observer.current.disconnect();

      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && !isReachingEnd) {
          setSize((prev) => prev + 1);
        }
      });

      if (node) observer.current.observe(node);
    },
    [isLoadingMore, isReachingEnd, setSize]
  );

  return {
    orders,
    isLoadingMore,
    isReachingEnd,
    error,
    lastItemRef,
    filters,
    updateFilter,
    updateFilters,
    resetFilters,
    removeFilter,
  };
};
