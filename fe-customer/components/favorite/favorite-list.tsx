"use client";

import { useFavoriteList } from "@/hooks/useFavoriteList";
import FavoriteProductItem from "./product-item";
import FavoriteListSkeleton from "./favorite-list-skeleton";

const FavoriteList = () => {
  const { data, isLoading, error, mutate } = useFavoriteList();
  const favList = data?.data.data;

  if (error) return <>Failed to load...</>;
  else if (isLoading || !data)
    return (
      <>
        <FavoriteListSkeleton />
      </>
    );
  return (
    <div className="grid lg:grid-cols-3 grid-cols-2 md:gap-4 gap-2 md:px-4">
      {favList?.map((item) => (
        <FavoriteProductItem
          key={item.id}
          product={item}
          onMutate={() => mutate()}
        />
      ))}
    </div>
  );
};

export default FavoriteList;
