"use client";
import OrderStatusView from "./order-status-view";
import OrderSteps from "./order-steps";
import OrderDetailItem from "./order-detail-item";
import { useOrder } from "@/hooks/useOrder";
import { Separator } from "../ui/separator";
import { toVND } from "@/lib/utils";

const OrderContent = ({ orderId }: { orderId: string }) => {
  const { data, isLoading, error } = useOrder({ orderId: orderId });

  const order = data?.data;
  if (error) return <>Failed to load</>;
  else if (isLoading || !order) return <>Skeleton...</>;
  else
    return (
      <div className="flex flex-1 justify-center grow w-full">
        <div className="flex flex-col gap-6 w-full max-w-[1200px]">
          <div className="flex justify-between">
            <span className="text-lg font-medium">Order ID: {order.id}</span>
            <OrderStatusView status={order.orderStatus} />
          </div>
          <Separator />
          <OrderSteps order={order} />
          {order.details.map((detail, index) => (
            <OrderDetailItem key={`order-detail-${index}`} detail={detail} />
          ))}
          <div className="grid grid-cols-[3fr_1fr]">
            <span className="text-fs-gray-dark ml-auto w-32">Total</span>
            <span className="text-black text-end font-medium">
              {toVND(order.totalPrice)}
            </span>
          </div>
        </div>
      </div>
    );
};

export default OrderContent;
