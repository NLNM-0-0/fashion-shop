import OrderContent from "@/components/order/order-content";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Order",
};
const OrderDetail = ({ params }: { params: { id: string } }) => {
  return (
    <>
      <OrderContent orderId={params.id} />
    </>
  );
};

export default OrderDetail;
