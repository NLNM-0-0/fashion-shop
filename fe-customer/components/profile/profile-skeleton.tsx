import { Skeleton } from "../ui/skeleton";

const ProfileSkeleton = () => {
  return (
    <div className="max-w-[800px] w-full">
      <div className="flex flex-col items-center gap-5 ">
        <div className="flex md:gap-16 sm:gap-8 gap-4 w-full">
          <Skeleton className="sm:h-[150px] sm:w-[150px] w-[80px] h-[80px] rounded-full mt-20"></Skeleton>
          <div className="flex flex-col gap-5 flex-1">
            <div className="flex justify-center gap-2">
              <Skeleton className="mb-6 h-12 w-36" />
            </div>
            <Skeleton className="h-12 w-full" />
            <Skeleton className="h-12 w-full" />
            <Skeleton className="h-12 w-full" />
            <Skeleton className="h-12 w-full" />
            <Skeleton className="h-12 w-full" />
            <Skeleton className="h-12 w-full" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileSkeleton;
