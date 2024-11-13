import DetailContent from "@/components/product/detail-content";
import { sampleProduct } from "@/lib/constants";
import React from "react";

const ProductDetailPage = () => {
  return (
    <>
      <DetailContent product={sampleProduct}/>
    </>
  );
};

export default ProductDetailPage;
