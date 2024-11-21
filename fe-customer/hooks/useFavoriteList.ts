import getAllFavorite from "@/lib/api/favorite/getAllFavorite";
import useSWR from "swr";

export const FAVORITE_KEY = "favorite-all";

export const useFavoriteList = () => {
  const { data, isLoading, error, mutate } = useSWR(FAVORITE_KEY, () =>
    getAllFavorite()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
