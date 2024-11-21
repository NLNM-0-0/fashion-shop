"use client";
import OrderStatusView from "./order-status-view";
import OrderSteps from "./order-steps";
import OrderDetailItem from "./order-detail-item";
import { useOrder } from "@/hooks/useOrder";
import { Separator } from "../ui/separator";
import { toVND } from "@/lib/utils";
import { Button } from "../ui/button";
import { updateOrderStatus } from "@/lib/api/order/updateOrderStatus";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { orderStatusToNextTitle } from "@/lib/constants";
import { cancelOrder } from "@/lib/api/order/cancelOrder";

const OrderContent = ({ orderId }: { orderId: string }) => {
  const { data, isLoading, error, mutate } = useOrder({ orderId: orderId });

  const order = data?.data;

  const handleUpdateOrderStatus = () => {
    updateOrderStatus(orderId)
      .then(() => {
        mutate();
        toast({
          variant: "success",
          title: "Success",
          description: "Update order status successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description:
            err.response?.data.message ?? "Update order status failed",
        });
      });
  };

  const handleCancelOrder = () => {
    cancelOrder(orderId)
      .then(() => {
        mutate();
        toast({
          variant: "success",
          title: "Success",
          description: "Cancel order successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Cancel order failed",
        });
      });
  };

  if (error) return <>Failed to load</>;
  else if (isLoading || !order) return <>Skeleton...</>;
  else
    return (
      <div className="flex flex-col gap-6">
        <div className="flex justify-between">
          <span className="text-xl">Order ID: {order.id}</span>
          <OrderStatusView status={order.orderStatus} />
        </div>
        <Separator />
        <div className="flex flex-1 lg:flex-row flex-col justify-center grow w-full gap-10">
          <div className="flex flex-col w-full max-w-[1200px]">
            <OrderSteps order={order} />
            {order.details.map((detail, index) => (
              <OrderDetailItem key={`order-detail-${index}`} detail={detail} />
            ))}
          </div>

          <div className="flex basis-1/3 w-full flex-col self-start min-w-96">
            <span className="table___title mb-10">Summary</span>
            <div className="flex justify-between">
              <span>Subtotal</span>
              <span>{toVND(order.totalPrice ?? 0)}</span>
            </div>
            <div className="flex justify-between mt-5">
              <span>Estimated Delivery & Handling</span>
              <span>Free</span>
            </div>
            <div className="flex justify-between mt-5 py-4 border-y">
              <span>
                Total ({order.totalQuantity} item
                {order.totalQuantity > 1 ? "s" : ""})
              </span>
              <span>
                <span>{toVND(order.totalPrice ?? 0)}</span>
              </span>
            </div>
            {orderStatusToNextTitle[order.orderStatus] && (
              <div className="flex flex-1 mt-5 gap-5">
                <Button
                  type="button"
                  onClick={handleCancelOrder}
                  variant={"destructive"}
                  className="h-12 flex-1 rounded-full"
                >
                  Cancel Order
                </Button>
                <Button
                  type="button"
                  onClick={handleUpdateOrderStatus}
                  className="h-12 flex-1 rounded-full"
                >
                  {orderStatusToNextTitle[order.orderStatus]}
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>
    );
};

export default OrderContent;
