"use client";
import { useHomeProductList } from "@/hooks/useHomeProductList";
import React from "react";
import ProductCarousel from "../product/product-carousel";
import FavoriteListSkeleton from "../favorite/favorite-list-skeleton";
import EssentialList from "./essential-list";
import Banner from "@/lib/assets/images/banner.png";
import Image from "next/image";

const HomeLayout = () => {
  const { data, isLoading, error } = useHomeProductList("latest");
  const productList = data?.data.data;
  if (!data || isLoading) {
    return <FavoriteListSkeleton />;
  } else if (error) return <>Failed to load.</>;
  return (
    <div className="flex flex-col gap-14">
      <Image
        src={Banner.src}
        alt="banner"
        height={4000}
        width={2700}
        className="w-full h-auto object-contain"
      />
      <ProductCarousel products={productList} title="Best Seller" />
      <ProductCarousel products={productList} title="Lastest" />
      <EssentialList />
    </div>
  );
};

export default HomeLayout;
