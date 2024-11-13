import SWRProvider from "@/components/auth/swr-provider";
import OrderListSkeleton from "@/components/order/order-list-skeleton";
import { ProductTable } from "@/components/product/table";
import { Metadata } from "next";
import React, { Suspense } from "react";

export const metadata: Metadata = {
  title: "Manage Product",
};

const ProductPage = () => {
  return (
    <Suspense fallback={<OrderListSkeleton number={5} />}>
      <SWRProvider>
        <ProductTable />
      </SWRProvider>
    </Suspense>
  );
};

export default ProductPage;
