"use client";
import { Order } from "@/lib/types";
import { getStatusTimeline } from "@/lib/utils";
import OrderStep from "./order-step";

const OrderSteps = ({ order }: { order: Order }) => {
  const orderStatusArray = getStatusTimeline(order);
  return orderStatusArray.length > 0 ? (
    <div className="flex w-full py-10 px-10">
      {orderStatusArray.map((item) => (
        <>
          <div className="flex flex-col items-center">
            <OrderStep key={item.label} status={item.label} />
            <div className="xl:text-lg text-base">{item.title}</div>
            <div className="text-fs-gray-darker xl:text-base text-sm">
              {item.time}
            </div>
          </div>
          <div className="h-0.5 bg-fs-success w-32 lg:mt-[50px] z-[-1] -ml-[40px] -mr-[40px] mt-[40px] last:hidden"></div>
        </>
      ))}
    </div>
  ) : null;
};

export default OrderSteps;
