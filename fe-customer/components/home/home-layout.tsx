"use client";
import {
  useBestSellerList,
  useLatestProductList,
} from "@/hooks/useHomeProductList";
import React from "react";
import ProductCarousel from "../product/product-carousel";
import FavoriteListSkeleton from "../favorite/favorite-list-skeleton";
import EssentialList from "./essential-list";
import Banner from "@/lib/assets/images/banner.png";
import Image from "next/image";

const HomeLayout = () => {
  const {
    data: latestList,
    isLoading: isLoadingLatest,
    error,
  } = useLatestProductList();
  const {
    data: bestSellerList,
    isLoading: isLoadingBestSeller,
    error: errorBestSeller,
  } = useBestSellerList();
  const productLatestList = latestList?.data.data;
  const productBestSellerList = bestSellerList?.data.data;

  if (
    !latestList ||
    !productBestSellerList ||
    isLoadingLatest ||
    isLoadingBestSeller
  ) {
    return <FavoriteListSkeleton />;
  } else if (error || errorBestSeller) return <>Failed to load.</>;
  return (
    <div className="flex flex-col gap-14">
      <Image
        src={Banner.src}
        alt="banner"
        height={4000}
        width={2700}
        className="w-full h-auto object-contain"
      />
      <ProductCarousel products={productBestSellerList} title="Best Seller" />
      <ProductCarousel products={productLatestList} title="Lastest" />
      <EssentialList />
    </div>
  );
};

export default HomeLayout;
