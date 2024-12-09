import encodeParams from "@/lib/helpers/params";
import { useRouter, useSearchParams } from "next/navigation";
import { useCallback, useEffect, useState, useRef, useMemo } from "react";

export interface FilterParams {
  [key: string]: string | string[] | null;
}

export const useFilteredList = () => {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [filters, setFilters] = useState<FilterParams>({});
  const filtersReady = useRef(false);

  const updateFilter = (filterName: string, value: string | string[]) => {
    setFilters((prev) => ({ ...prev, [filterName]: value }));
  };

  const resetFilters = useCallback(() => {
    const initialFilters: FilterParams = {};
    searchParams.forEach((value: string, key: string) => {
      const existingValue = initialFilters[key];
      if (existingValue) {
        if (Array.isArray(existingValue)) {
          existingValue.push(value);
        } else {
          initialFilters[key] = [existingValue, value];
        }
      } else {
        initialFilters[key] = value;
      }
    });
    if (JSON.stringify(filters) !== JSON.stringify(initialFilters)) {
      setFilters(initialFilters);
    }
    filtersReady.current = true;
  }, [searchParams, filters]);

  const removeFilter = (filterName: string) => {
    setFilters((prev) => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { [filterName]: first, ...remainingFilters } = prev;
      return remainingFilters;
    });
  };

  const updateFilters = (newFilters: FilterParams) => {
    setFilters((prev) => ({
      ...prev,
      ...newFilters,
    }));
  };

  const updateFilterUrl = (newFilters: FilterParams) => {
    const params = {
      ...filters,
      ...newFilters,
    };
    router.push(`/fa/products?${encodeParams(params)}`);
  };
  useEffect(() => {
    resetFilters();
  }, [resetFilters]);

  return {
    filters: useMemo(() => filters, [filters]),
    updateFilter,
    updateFilterUrl,
    updateFilters,
    resetFilters,
    removeFilter,
    filtersReady,
  };
};
