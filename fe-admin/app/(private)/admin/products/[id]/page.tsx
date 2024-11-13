import SWRProvider from "@/components/auth/swr-provider";
import EditProduct from "@/components/product/edit-product";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Update Product",
};
const EditProductPage = ({ params }: { params: { id: string } }) => {
  return (
    <SWRProvider>
      <h1 className="table___title">Update Product</h1>
      <EditProduct params={params} />
    </SWRProvider>
  );
};

export default EditProductPage;
