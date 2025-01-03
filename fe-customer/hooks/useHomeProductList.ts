import useSWR from "swr";

import getLatestProduct from "@/lib/api/product/getLastestProduct";
import getBestSeller from "@/lib/api/product/getBestSeller";

export const useLatestProductList = () => {
  const { data, isLoading, error, mutate } = useSWR(`product-latest`, () =>
    getLatestProduct()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};

export const useBestSellerList = () => {
  const { data, isLoading, error, mutate } = useSWR(`product-best-seller`, () =>
    getBestSeller()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
