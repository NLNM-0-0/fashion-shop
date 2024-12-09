import SWRProvider from "@/components/auth/swr-provider";
import OrderListSkeleton from "@/components/order/order-list-skeleton";
import AllProductLayout from "@/components/product/all-product-layout";
import { Metadata } from "next";
import React, { Suspense } from "react";

export const metadata: Metadata = {
  title: "Products",
};

const ProductPage = () => {
  return (
    <Suspense fallback={<OrderListSkeleton number={5} />}>
      <SWRProvider>
        <AllProductLayout />
      </SWRProvider>
    </Suspense>
  );
};

export default ProductPage;
