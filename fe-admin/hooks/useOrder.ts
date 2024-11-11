import useSWR from "swr";
import getOrder from "@/lib/api/order/getOrder";

export const useOrder = ({ orderId }: { orderId: string }) => {
  const { data, isLoading, error, mutate } = useSWR(
    `order-detail-${orderId}`,
    () => getOrder(orderId)
  );

  return {
    data,
    isLoading,
    error,
    mutate,
  };
};
