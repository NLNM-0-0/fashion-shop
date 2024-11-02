import SWRProvider from "@/components/auth/swr-provider";
import { CustomerTable } from "@/components/customer/table";

const CustomerPage = () => {
  return (
    <SWRProvider>
      <CustomerTable />
    </SWRProvider>
  );
};

export default CustomerPage;
