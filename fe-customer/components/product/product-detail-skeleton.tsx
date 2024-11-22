import { Skeleton } from "../ui/skeleton";

const ProductDetailSkeleton = () => {
  return (
    <div className="flex xl:flex-row flex-col gap-9 w-full justify-center items-center">
      <Skeleton className="md:w-[600px] sm:w-[500px] md:h-[700px] sm:h-[580px] w-full h-[600px] relative rounded-2xl overflow-clip"></Skeleton>
      <div className="flex-1 flex flex-col w-full xl:max-w-[400px] gap-2">
        <Skeleton className="h-12 w-3/5" />
        <Skeleton className="h-8 w-24" />
        <Skeleton className="h-8 w-16" />
        <Skeleton className="h-12 w-32" />
        <Skeleton className="h-12 w-full mt-24" />
        <Skeleton className="h-12 w-full" />
      </div>
    </div>
  );
};

export default ProductDetailSkeleton;
