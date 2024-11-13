"use client";

import { Product } from "@/lib/types";
import ProductImage from "./product-image";
import ProductContentForm from "./product-content-form";

const DetailContent = ({ product }: { product: Product }) => {
  return (
    <div className="flex gap-9 w-full justify-center">
      <ProductImage images={product.images} />
      <div className="max-w-[420px]">
        <ProductContentForm product={product} />
      </div>
    </div>
  );
};

export default DetailContent;
