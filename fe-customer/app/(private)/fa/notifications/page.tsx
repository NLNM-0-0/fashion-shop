import { AuthProvider } from "@/components/auth/auth-context";
import SWRProvider from "@/components/auth/swr-provider";
import NotiListSkeleton from "@/components/notification/noti-list-skeleton";
import NotificationList from "@/components/notification/notification-list";
import { Metadata } from "next";
import { Suspense } from "react";

export const metadata: Metadata = {
  title: "Notifications",
};
const NotificationPage = () => {
  return (
    <>
      <Suspense fallback={<NotiListSkeleton number={3} />}>
        <SWRProvider>
          <AuthProvider>
            <NotificationList />
          </AuthProvider>
        </SWRProvider>
      </Suspense>
    </>
  );
};

export default NotificationPage;
