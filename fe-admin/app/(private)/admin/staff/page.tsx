import SWRProvider from "@/components/auth/swr-provider";
import { StaffTable } from "@/components/staff/table";

const StaffPage = () => {
  return (
    <SWRProvider>
      <StaffTable />
    </SWRProvider>
  );
};

export default StaffPage;
