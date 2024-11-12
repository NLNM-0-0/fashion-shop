import useSWR from "swr";
import getAllCategory from "@/lib/api/product/getAllCategory";

export const CATEGORY_KEY = "category-all";

export const useCategoryList = () => {
  const { data, isLoading, error, mutate } = useSWR(CATEGORY_KEY, () =>
    getAllCategory()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
