import { useSearchParams } from "next/navigation";
import { useCallback, useEffect, useState, useRef, useMemo } from "react";

export interface FilterParams {
  [key: string]: string;
}

export const useFilteredList = () => {
  const searchParams = useSearchParams();
  const [filters, setFilters] = useState<FilterParams>({});
  const filtersReady = useRef(false);

  const updateFilter = (filterName: string, value: string) => {
    setFilters((prev) => ({ ...prev, [filterName]: value }));
  };

  const resetFilters = useCallback(() => {
    const initialFilters: FilterParams = {};
    searchParams.forEach((value: string, key: string) => {
      initialFilters[key] = value;
    });
    setFilters(initialFilters);
    filtersReady.current = true;
  }, [searchParams]);

  useEffect(() => {
    resetFilters();
  }, [resetFilters]);

  const removeFilter = (filterName: string) => {
    setFilters((prev) => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { [filterName]: first, ...remainingFilters } = prev;
      return remainingFilters;
    });
  };

  return {
    filters: useMemo(() => filters, [filters]),
    updateFilter,
    resetFilters,
    removeFilter,
    filtersReady,
  };
};
