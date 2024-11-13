import SWRProvider from "@/components/auth/swr-provider";
import OrderList from "@/components/order/order-list";
import OrderListSkeleton from "@/components/order/order-list-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";

export const metadata: Metadata = {
  title: "Manage Order",
};

const OrderListPage = () => {
  return (
    <Suspense fallback={<OrderListSkeleton number={5} />}>
      <SWRProvider>
        <OrderList />
      </SWRProvider>
    </Suspense>
  );
};

export default OrderListPage;
