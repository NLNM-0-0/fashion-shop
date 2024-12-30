import SWRProvider from "@/components/auth/swr-provider";
import FavoriteList from "@/components/favorite/favorite-list";
import FavoriteListSkeleton from "@/components/favorite/favorite-list-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";
export const metadata: Metadata = {
  title: "My Favorite",
};
const FavoritePage = () => {
  return (
    <>
      <Suspense fallback={<FavoriteListSkeleton />}>
        <SWRProvider>
          <FavoriteList />
        </SWRProvider>
      </Suspense>
    </>
  );
};

export default FavoritePage;
