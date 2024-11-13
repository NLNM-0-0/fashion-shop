import ProfileLayout from "@/components/profile/profile-layout";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "My Profile",
};
const ProfilePage = () => {
  return (
    <>
      <ProfileLayout />
    </>
  );
};

export default ProfilePage;
