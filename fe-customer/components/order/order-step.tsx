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
        "lg:h-[100px] h-20 lg:w-[100px] w-20 flex justify-center items-center rounded-full",
        isCurrent && "bg-fs-success",
        !isCurrent && "bg-white border-4 border-fs-success"
      )}
      style={{ color: "blue" }}
    >
      <IconComponent
        className={clsx(
          "lg:h-12 lg:w-12 h-10 w-10",
          isCurrent && "text-white",
          !isCurrent && "text-fs-success"
        )}
      />
    </div>
  );
};

export default OrderStep;
