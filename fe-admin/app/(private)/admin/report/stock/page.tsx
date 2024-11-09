import SWRProvider from "@/components/auth/swr-provider";
import { StockReportTable } from "@/components/report/stock/table";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Stock Report",
};

const StockReportPage = () => {
  return (
    <SWRProvider>
      <StockReportTable />
    </SWRProvider>
  );
};

export default StockReportPage;
