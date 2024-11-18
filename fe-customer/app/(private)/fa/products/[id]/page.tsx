import DetailContent from "@/components/product/detail-content";
import React from "react";

const ProductDetailPage = ({ params }: { params: { id: string } }) => {
  return (
    <>
      <DetailContent id={params.id} />
    </>
  );
};

export default ProductDetailPage;
