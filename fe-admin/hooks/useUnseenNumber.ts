import useSWR from "swr";
import getUnseenNumber from "@/lib/api/notification/getUnseenNumber";

export const UNSEEN_KEY = "unseen-notification";

export const useUnseenNumber = () => {
  const { data, isLoading, error, mutate } = useSWR(UNSEEN_KEY, () =>
    getUnseenNumber()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
