import SignupForm from "@/components/auth/signup/signup-form";
import { Metadata } from "next";
import Image from "next/image";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Sign Up",
};

const Signup = () => {
  return (
    <div className="flex flex-col w-full h-full justify-center items-center">
      <Link href={"/fa"}>
        <Image
          src={"/android-chrome-192x192.png"}
          alt="logo"
          width={100}
          height={100}
        />
      </Link>
      <SignupForm />
      <div className="text-sm mt-auto">
        <span className="text-fs-gray-dark">Already a Member? </span>
        <Link className="underline underline-offset-2" href={"/login"}>
          Sign In
        </Link>
      </div>
    </div>
  );
};

export default Signup;
