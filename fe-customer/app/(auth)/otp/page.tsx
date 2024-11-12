import VerifyOTPForm from "@/components/auth/otp/otp-form";
import Image from "next/image";
import Link from "next/link";
const VerifyOTP = () => {
  return (
    <div className="flex flex-col w-full h-full py-20 items-center">
      <Image
        src={"/android-chrome-192x192.png"}
        alt="logo"
        width={100}
        height={100}
      />
      <VerifyOTPForm />
      <div className="text-sm mt-auto">
        <span className="text-fs-gray-dark">Already a Member? </span>
        <Link className="underline underline-offset-2" href={"/login"}>
          Sign In
        </Link>
      </div>
    </div>
  );
};

export default VerifyOTP;
