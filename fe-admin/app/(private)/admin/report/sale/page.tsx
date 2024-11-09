import SWRProvider from "@/components/auth/swr-provider";
import { SaleReportTable } from "@/components/report/sale/table";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Sale Report",
};

const SaleReportPage = () => {
  return (
    <SWRProvider>
      <SaleReportTable />
    </SWRProvider>
  );
};

export default SaleReportPage;
