import useSWR from "swr";
import getCartNumber from "@/lib/api/cart/getCartNumber";

export const CART_NUMBER_KEY = "card-number-key";

export const useCartNumber = () => {
  const { data, isLoading, error, mutate } = useSWR(CART_NUMBER_KEY, () =>
    getCartNumber()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
