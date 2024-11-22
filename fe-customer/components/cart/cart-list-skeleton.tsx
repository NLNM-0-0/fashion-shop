import { Skeleton } from "../ui/skeleton";

const CartListSkeleton = () => {
  return (
    <div className="flex gap-10 lg:flex-row flex-col">
      <div className="flex basis-2/3 flex-col">
        <Skeleton className="h-12 w-32 mb-4"></Skeleton>
        {[...Array(5)].map((_, index) => (
          <div
            className="flex gap-4 pt-6 py-10 border-b lg:last:border-b-0"
            key={`cart-skeleton-${index}`}
          >
            <div className="flex flex-col gap-3">
              <Skeleton className="h-[164px] w-[164px]" />
              <Skeleton className="w-full h-10" />
            </div>
            <div className="flex-1 flex justify-between sm:flex-row flex-col-reverse gap-1">
              <div className="flex-1 flex flex-col gap-1 text-base">
                <Skeleton className="h-8 w-44" />
                <Skeleton className="h-8 w-24" />
                <Skeleton className="h-8 w-16" />
              </div>
              <Skeleton className="h-8 w-28" />
            </div>
          </div>
        ))}
      </div>

      <div className="flex basis-1/3 w-full flex-col self-start min-w-96">
        <Skeleton className="h-12 w-32 mb-10"></Skeleton>
        <div className="flex justify-between">
          <Skeleton className="h-8 w-20"></Skeleton>
          <Skeleton className="h-8 w-32"></Skeleton>
        </div>
        <div className="flex justify-between mt-5">
          <Skeleton className="h-8 w-44"></Skeleton>
          <Skeleton className="h-8 w-16"></Skeleton>
        </div>
        <div className="flex justify-between mt-5 py-4 border-y">
          <Skeleton className="h-8 w-12"></Skeleton>
          <Skeleton className="h-8 w-32"></Skeleton>
        </div>
        <Skeleton className="h-12 w-full mt-5"></Skeleton>
      </div>
    </div>
  );
};

export default CartListSkeleton;
