import SWRProvider from "@/components/auth/swr-provider";
import OrderList from "@/components/order/order-list";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Manage Order",
};

const OrderListPage = () => {
  return (
    <SWRProvider>
      <OrderList />
    </SWRProvider>
  );
};

export default OrderListPage;
