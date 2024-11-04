import SWRProvider from "@/components/auth/swr-provider";
import { CustomerTable } from "@/components/customer/table";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Manage Customer",
};

const CustomerPage = () => {
  return (
    <SWRProvider>
      <CustomerTable />
    </SWRProvider>
  );
};

export default CustomerPage;
