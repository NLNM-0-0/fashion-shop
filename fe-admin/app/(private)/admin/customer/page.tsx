import SWRProvider from "@/components/auth/swr-provider";
import { CustomerTable } from "@/components/customer/table";
import TableSkeleton from "@/components/table-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";

export const metadata: Metadata = {
  title: "Manage Customer",
};

const CustomerPage = () => {
  return (
    <Suspense
      fallback={
        <TableSkeleton
          isHasExtensionAction={false}
          isHasFilter={true}
          isHasSearch={true}
          isHasChooseVisibleRow={false}
          isHasCheckBox={false}
          isHasPaging={true}
          numberRow={5}
          cells={[
            {
              percent: 1,
            },
            {
              percent: 5,
            },
            {
              percent: 1,
            },
          ]}
        />
      }
    >
      <SWRProvider>
        <CustomerTable />
      </SWRProvider>
    </Suspense>
  );
};

export default CustomerPage;
