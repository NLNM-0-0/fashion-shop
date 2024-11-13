"use client";

import { useProduct } from "@/hooks/useProduct";
import EditProductContent from "./edit-product-content";

const EditProduct = ({ params }: { params: { id: string } }) => {
  const { data, error, isLoading } = useProduct(params);
  if (!data || isLoading) return <>Skeleton</>;
  else if (error) return <>Failed to load</>;
  return (
    <div>
      <EditProductContent product={data.data ?? undefined} />
    </div>
  );
};

export default EditProduct;
