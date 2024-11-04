import SWRProvider from "@/components/auth/swr-provider";
import { StaffTable } from "@/components/staff/table";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Manage Staff",
};

const StaffPage = () => {
  return (
    <SWRProvider>
      <StaffTable />
    </SWRProvider>
  );
};

export default StaffPage;
