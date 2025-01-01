import useSWR from "swr";
import getCartNumber from "@/lib/api/cart/getCartNumber";
import { UserInfo } from "@/lib/types";

export const CART_NUMBER_KEY = "card-number-key";

export const useCartNumber = (user: UserInfo | null) => {
  const { data, isLoading, error, mutate } = useSWR(
    user ? CART_NUMBER_KEY : null,
    () => getCartNumber()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
