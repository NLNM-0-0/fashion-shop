import SWRProvider from "@/components/auth/swr-provider";
import { ProductTable } from "@/components/product/table";
import { Metadata } from "next";
import React from "react";

export const metadata: Metadata = {
  title: "Manage Product",
};

const ProductPage = () => {
  return (
    <SWRProvider>
      <ProductTable />
    </SWRProvider>
  );
};

export default ProductPage;
