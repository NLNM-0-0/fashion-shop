import SWRProvider from "@/components/auth/swr-provider";
import OrderContent from "@/components/order/order-content";
import OrderListSkeleton from "@/components/order/order-list-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";

export const metadata: Metadata = {
  title: "Order",
};
const OrderDetail = ({ params }: { params: { id: string } }) => {
  return (
    <>
      <Suspense fallback={<OrderListSkeleton number={2} />}>
        <SWRProvider>
          <OrderContent orderId={params.id} />
        </SWRProvider>
      </Suspense>
    </>
  );
};

export default OrderDetail;
