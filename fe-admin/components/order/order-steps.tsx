"use client";
import { Order } from "@/lib/types";
import { getStatusTimeline } from "@/lib/utils";
import OrderStep from "./order-step";

const OrderSteps = ({ order }: { order: Order }) => {
  const orderStatusArray = getStatusTimeline(order);

  return (
    <div className="flex">
      {orderStatusArray.map((item) => (
        <OrderStep key={item.label} status={item.label} />
      ))}
    </div>
  );
};

export default OrderSteps;
