import SWRProvider from "@/components/auth/swr-provider";
import ProfileLayout from "@/components/profile/profile-layout";
import ProfileSkeleton from "@/components/profile/profile-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";

export const metadata: Metadata = {
  title: "My Profile",
};
const ProfilePage = () => {
  return (
    <>
      <Suspense fallback={<ProfileSkeleton />}>
        <SWRProvider>
          <ProfileLayout />
        </SWRProvider>
      </Suspense>
    </>
  );
};

export default ProfilePage;
