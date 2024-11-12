import SendEmailForgotPasswordForm from "@/components/auth/reset-password/send-email-forgot-password-form";
import { Metadata } from "next";
import Image from "next/image";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Forget Password",
};
const ForgotPassword = () => {
  return (
    <div className="flex flex-col w-full h-full py-20 justify-center items-center">
      <Image
        src={"/android-chrome-192x192.png"}
        alt="logo"
        width={100}
        height={100}
      />
      <SendEmailForgotPasswordForm />
      <div className="text-sm mt-auto">
        <span className="text-fs-gray-dark">Already a Member? </span>
        <Link className="underline underline-offset-2" href={"/login"}>
          Sign In
        </Link>
      </div>
    </div>
  );
};

export default ForgotPassword;
