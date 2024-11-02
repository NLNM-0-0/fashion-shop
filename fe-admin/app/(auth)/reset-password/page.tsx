import SendEmailForgotPasswordForm from "@/components/auth/reset-password/send-email-forgot-password-form";
import { Metadata } from "next";
import Image from "next/image";

export const metadata: Metadata = {
  title: "Forget Password",
};
const ForgotPassword = () => {
  return (
    <div className="flex flex-col w-full h-full justify-center items-center">
      <Image
        src={"/android-chrome-192x192.png"}
        alt="logo"
        width={100}
        height={100}
      />
      <SendEmailForgotPasswordForm />
    </div>
  );
};

export default ForgotPassword;
