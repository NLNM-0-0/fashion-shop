"use client";
import { Order } from "@/lib/types";
import { getStatusTimeline } from "@/lib/utils";
import OrderStep from "./order-step";

const OrderSteps = ({ order }: { order: Order }) => {
  const orderStatusArray = getStatusTimeline(order);
  return orderStatusArray.length > 0 ? (
    <div className="flex lg:flex-row flex-col lg:p-10 p-5 justify-center">
      {orderStatusArray.map((item) => (
        <>
          <div className="flex lg:flex-col items-center">
            <OrderStep key={item.label} status={item.label} />
            <div className="xl:text-lg sm:text-base text-sm lg:ml-0 ml-2 whitespace-nowrap">
              {item.title}
            </div>
            <div className="text-fs-gray-darker xl:text-base sm:text-sm text-xs lg:ml-0 ml-1">
              {item.time}
            </div>
          </div>
          <div className="lg:h-0.5 h-24 bg-fs-success lg:w-32 w-0.5 lg:mt-[50px] -mt-7 z-[-1] lg:-ml-[40px] ml-7 lg:-mr-[40px] mr-auto lg:mb-auto -mb-7 last:hidden"></div>
        </>
      ))}
    </div>
  ) : null;
};

export default OrderSteps;
