import AddNewProduct from "@/components/product/add-new-product";
import { Metadata } from "next";
import React from "react";

export const metadata: Metadata = {
  title: "Add New Product",
};
const NewProduct = () => {
  return (
    <>
      <h1 className="table___title">Add New Product</h1>
      <AddNewProduct />
    </>
  );
};

export default NewProduct;
