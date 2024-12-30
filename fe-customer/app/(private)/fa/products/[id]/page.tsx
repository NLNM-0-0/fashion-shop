import SWRProvider from "@/components/auth/swr-provider";
import DetailContent from "@/components/product/detail-content";
import ProductDetailSkeleton from "@/components/product/product-detail-skeleton";
import React, { Suspense } from "react";

const ProductDetailPage = ({ params }: { params: { id: string } }) => {
  return (
    <>
      <Suspense fallback={<ProductDetailSkeleton />}>
        <SWRProvider>
          <DetailContent id={params.id} />
        </SWRProvider>
      </Suspense>
    </>
  );
};

export default ProductDetailPage;
