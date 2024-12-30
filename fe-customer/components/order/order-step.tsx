"use client";
import { orderStatusIcons } from "@/lib/constants";
import { OrderStatus } from "@/lib/constants/enum";
import clsx from "clsx";
interface OrderStepProps {
  status: OrderStatus;
  isCurrent?: boolean;
}
const OrderStep = ({ status, isCurrent }: OrderStepProps) => {
  const IconComponent = orderStatusIcons[status];
  return (
    <div
      className={clsx(
        "lg:h-[100px] h-14 lg:w-[100px] w-14 flex justify-center items-center rounded-full lg:mb-4",
        isCurrent && "bg-fs-success",
        !isCurrent && "bg-white lg:border-4 border-[3px] border-fs-success"
      )}
    >
      <IconComponent
        className={clsx(
          "lg:h-12 lg:w-12 h-7 w-7",
          isCurrent && "text-white",
          !isCurrent && "text-fs-success"
        )}
      />
    </div>
  );
};

export default OrderStep;
