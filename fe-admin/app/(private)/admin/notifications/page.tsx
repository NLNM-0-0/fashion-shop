import { AuthProvider } from "@/components/auth/auth-context";
import NotificationList from "@/components/notification/notification-list";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Notifications",
};
const NotificationPage = () => {
  return (
    <>
      <AuthProvider>
        <NotificationList />
      </AuthProvider>
    </>
  );
};

export default NotificationPage;
