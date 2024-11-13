import React from "react";
import { Skeleton } from "../ui/skeleton";
import { Separator } from "../ui/separator";

const EditContentSkeleton = () => {
  return (
    <div className="card___style flex flex-col w-full py-12">
      <div className="flex lg:flex-row flex-col gap-10">
        <div className="flex flex-1 flex-col text-gray-text">
          <Skeleton className="mt-5 h-10 w-full" />

          <Skeleton className="mt-5 h-10 w-full" />

          <div className="flex flex-1 gap-5 md:flex-row flex-col-reverse">
            <div className="flex-1">
              <Skeleton className="mt-5 h-12 w-full" />
              <Skeleton className="mt-5 h-12 w-full" />
            </div>
            <div className="flex-1">
              <Skeleton className="mt-5 h-12 w-full" />
            </div>
          </div>
        </div>
        <div className="min-w-72 basis-1/3 flex gap-3">
          <Skeleton className="mt-5 h-24 flex-1" />
          <Skeleton className="mt-5 h-24 flex-1" />
          <Skeleton className="mt-5 h-24 flex-1" />
        </div>
      </div>
      <Separator className="my-10 bg-fs-gray-light" />
      <Skeleton className="mt-5 h-12 w-full" />
      <Skeleton className="mt-5 h-12 w-full" />

      <Skeleton className="mt-5 h-44 w-full" />
    </div>
  );
};

export default EditContentSkeleton;
