import React from "react";
import { Skeleton } from "../ui/skeleton";

const OrderListSkeleton = ({ number }: { number: number }) => {
  return (
    <div className="flex flex-col gap-4">
      {[...Array(number)].map((_, index) => (
        <div
          key={index}
          className={`flex flex-col p-4 gap-2 rounded-xl shadow-[0px_1px_2px_0px_rgba(0,0,0,0.1)] hover:shadow-md border overflow-clip`}
        >
          <Skeleton className="h-12 w-full" />
          <Skeleton className="h-20 w-full" />
          <Skeleton className="h-12 w-full" />
        </div>
      ))}
    </div>
  );
};

export default OrderListSkeleton;
