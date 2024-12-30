import useSWR from "swr";

import getAllProduct from "@/lib/api/product/getAllProduct";

export const useHomeProductList = (key: string) => {
  const { data, isLoading, error, mutate } = useSWR(`product-${key}`, () =>
    getAllProduct({})
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
