import useSWR from "swr";
import getUnseenNumber from "@/lib/api/notification/getUnseenNumber";
import { UserInfo } from "@/lib/types";

export const UNSEEN_KEY = "unseen-notification";

export const useUnseenNumber = (user: UserInfo | null) => {
  const { data, isLoading, error, mutate } = useSWR(
    user ? UNSEEN_KEY : null,
    () => getUnseenNumber()
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
