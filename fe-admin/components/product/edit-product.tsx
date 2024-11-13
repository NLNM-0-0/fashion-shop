"use client";

import { useProduct } from "@/hooks/useProduct";
import EditProductContent from "./edit-product-content";
import EditContentSkeleton from "./edit-content-skeleton";

const EditProduct = ({ params }: { params: { id: string } }) => {
  const { data, error, isLoading } = useProduct(params);
  if (!data || isLoading)
    return (
      <>
        <EditContentSkeleton />
      </>
    );
  else if (error) return <>Failed to load</>;
  return (
    <div>
      <EditProductContent product={data.data ?? undefined} />
    </div>
  );
};

export default EditProduct;
