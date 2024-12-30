import { Skeleton } from "../ui/skeleton";

const PaymentSkeleton = () => {
  return (
    <div className="flex gap-10 lg:flex-row flex-col-reverse w-full lg:max-w-[1000px] max-w-[620px]">
      <div className="flex basis-2/3 flex-col">
        <Skeleton className="h-10 mb-4 w-44" />
        <Skeleton className="h-12 mb-1 w-full" />
        <Skeleton className="h-12 mt-5 mb-1 w-full" />
        <Skeleton className="h-12 mt-5 mb-1 w-full" />

        <Skeleton className="h-12 mb-4 mt-10 w-full" />
        <Skeleton className="h-12 w-full" />
        <Skeleton className="h-12 mt-16 w-full" />
      </div>

      <div className="flex basis-1/3 w-full flex-col self-start min-w-96">
        <Skeleton className="h-12 mb-4 w-full" />
        <Skeleton className="h-28 mb-4 w-full" />
      </div>
    </div>
  );
};

export default PaymentSkeleton;
