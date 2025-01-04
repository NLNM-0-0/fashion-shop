"use client";
import OrderStatusView from "./order-status-view";
import OrderSteps from "./order-steps";
import OrderDetailItem from "./order-detail-item";
import { useOrder } from "@/hooks/useOrder";
import { Separator } from "../ui/separator";
import { toVND } from "@/lib/utils";
import { Button } from "../ui/button";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";

import { OrderStatus } from "@/lib/constants/enum";
import { cancelOrder } from "@/lib/api/order/cancelOrder";
import { receiveOrder } from "@/lib/api/order/receiveOrder";
import OrderListSkeleton from "./order-list-skeleton";

const OrderContent = ({ orderId }: { orderId: string }) => {
  const { data, isLoading, error, mutate } = useOrder({ orderId: orderId });

  const order = data?.data;

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

  const handleReceiveOrder = () => {
    receiveOrder(orderId)
      .then(() => {
        mutate();
        toast({
          variant: "success",
          title: "Success",
          description: "Receive order successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Receive order failed",
        });
      });
  };

  if (error) return <>Failed to load</>;
  else if (isLoading || !order)
    return (
      <>
        <OrderListSkeleton number={5} />
      </>
    );
  else
    return (
      <div className="flex flex-col gap-6 pb-10">
        <div className="flex justify-between">
          <div className="md:text-xl md:flex-row flex-col text-base flex gap-2">
            <span>Order ID: {order.id}</span>
            <span className="md:block hidden">|</span>
            <span>Order date: {order.createdAt}</span>
          </div>
          <OrderStatusView status={order.orderStatus} />
        </div>
        <Separator />
        <OrderSteps order={order} />
        <div className="flex flex-1 lg:flex-row flex-col justify-center grow w-full gap-10">
          <div className="flex flex-col w-full max-w-[1200px]">
            {order.details.map((detail, index) => (
              <OrderDetailItem key={`order-detail-${index}`} detail={detail} />
            ))}
          </div>

          <div className="flex basis-1/3 w-full flex-col self-start xl:min-w-96 min-w-80">
            <span className="table___title mb-10">Contact information</span>
            <div className="flex justify-between">
              <span>Name</span>
              <span>{order.name}</span>
            </div>
            <div className="flex justify-between mt-5">
              <span>Phone number</span>
              <span>{order.phone}</span>
            </div>
            <div className="flex justify-between mt-5 border-b mb-10 pb-4">
              <span>Address</span>
              <span>
                <span>{order.address}</span>
              </span>
            </div>
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
            <div className="flex flex-1 mt-5 gap-5">
              {order.orderStatus === OrderStatus.PENDING && (
                <Button
                  type="button"
                  onClick={handleCancelOrder}
                  variant={"destructive"}
                  className="h-12 flex-1 rounded-full"
                >
                  Cancel Order
                </Button>
              )}
              {order.orderStatus === OrderStatus.SHIPPING && (
                <Button
                  type="button"
                  onClick={handleReceiveOrder}
                  className="h-12 flex-1 rounded-full"
                >
                  Receive Order
                </Button>
              )}
            </div>
          </div>
        </div>
      </div>
    );
};

export default OrderContent;
