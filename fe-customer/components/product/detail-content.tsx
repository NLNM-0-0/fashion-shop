"use client";

import ProductImage from "./product-image";
import ProductContentForm from "./product-content-form";
import { useProduct } from "@/hooks/useProduct";
import ProductDetailSkeleton from "./product-detail-skeleton";

const DetailContent = ({ id }: { id: string }) => {
  const { data, isLoading, error } = useProduct({ id: id });
  const product = data?.data;

  if (error) return <>Failed to load.</>;
  else if (isLoading || !data || !product)
    return (
      <>
        <ProductDetailSkeleton />
      </>
    );
  else
    return (
      <div className="flex xl:flex-row flex-col gap-9 w-full justify-center items-center">
        <ProductImage images={product.images} />
        <div className="flex-1 w-full xl:max-w-[400px]">
          <ProductContentForm product={product} />
        </div>
      </div>
    );
};

export default DetailContent;
