import SWRProvider from "@/components/auth/swr-provider";
import { ProductTable } from "@/components/product/table";
import TableSkeleton from "@/components/table-skeleton";
import { Metadata } from "next";
import React, { Suspense } from "react";

export const metadata: Metadata = {
  title: "Manage Product",
};

const ProductPage = () => {
  return (
    <Suspense
      fallback={
        <TableSkeleton
          isHasExtensionAction={false}
          isHasFilter={true}
          isHasSearch={true}
          isHasChooseVisibleRow={false}
          isHasCheckBox={false}
          isHasPaging={true}
          numberRow={5}
          cells={[
            {
              percent: 1,
            },
            {
              percent: 5,
            },
            {
              percent: 1,
            },
          ]}
        ></TableSkeleton>
      }
    >
      <SWRProvider>
        <ProductTable />
      </SWRProvider>
    </Suspense>
  );
};

export default ProductPage;
