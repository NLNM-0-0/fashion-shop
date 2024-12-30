import SWRProvider from "@/components/auth/swr-provider";
import FavoriteListSkeleton from "@/components/favorite/favorite-list-skeleton";
import HomeLayout from "@/components/home/home-layout";
import { Suspense } from "react";

export default function Home() {
  return (
    <>
      <Suspense fallback={<FavoriteListSkeleton />}>
        <SWRProvider>
          <HomeLayout />
        </SWRProvider>
      </Suspense>
    </>
  );
}
