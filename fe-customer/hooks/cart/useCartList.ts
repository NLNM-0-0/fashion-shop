import useSWR from "swr";
import getCartList from "@/lib/api/cart/getCartList";

export const CART_KEY = "cart-all";

export const useCartList = () => {
  const { data, isLoading, error, mutate } = useSWR(CART_KEY, () =>
    getCartList()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
