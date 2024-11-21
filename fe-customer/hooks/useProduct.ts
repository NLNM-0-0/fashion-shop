import getProduct from "@/lib/api/product/getProduct";
import useSWR from "swr";

export const useProduct = ({ id }: { id: string }) => {
  const { data, isLoading, error, mutate } = useSWR(
    `product-detail-${id}`,
    () => getProduct(id)
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
