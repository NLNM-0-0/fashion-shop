import React from "react";
import { AspectRatio } from "../ui/aspect-ratio";
import { Skeleton } from "../ui/skeleton";

const FavoriteListSkeleton = () => {
  return (
    <div className="grid lg:grid-cols-3 grid-cols-2 md:gap-4 gap-2 md:px-4">
      {[...Array(5)].map((_, index) => (
        <div
          className="flex flex-col bg-white pb-10"
          key={`fav-list-skeleton-${index}`}
        >
          <AspectRatio ratio={1 / 1} className="bg-muted">
            <Skeleton className="h-full w-full rounded-md" />
          </AspectRatio>
          <div className="flex justify-between mt-3 items-start">
            <div className="flex-1">
              <Skeleton className="h-8 md:w-3/5 w-4/5"></Skeleton>
              <Skeleton className="h-8 w-20 mt-1"></Skeleton>
            </div>
            <Skeleton className="h-10 w-10"></Skeleton>
          </div>
        </div>
      ))}
    </div>
  );
};

export default FavoriteListSkeleton;
