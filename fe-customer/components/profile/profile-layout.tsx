import { AuthProvider } from "../auth/auth-context";
import ProfileForm from "./profile-form";

const ProfileLayout = () => {
  return (
    <AuthProvider>
      <div className="flex w-full justify-center">
        <ProfileForm />
      </div>
    </AuthProvider>
  );
};

export default ProfileLayout;
