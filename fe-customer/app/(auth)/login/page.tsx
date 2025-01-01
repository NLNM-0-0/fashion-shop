import LoginForm from "@/components/auth/login/login-form";
import { Metadata } from "next";
import Image from "next/image";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Login",
};
const Login = () => {
  return (
    <div className="flex flex-col w-full h-full py-20 items-center">
      <Link href={"/fa"}>
        <Image
          src={"/android-chrome-192x192.png"}
          alt="logo"
          width={100}
          height={100}
        />
      </Link>
      <LoginForm />
      <div className="text-sm mt-auto">
        <span className="text-fs-gray-dark">Don&#39;t have an account? </span>
        <Link className="underline underline-offset-2" href={"/signup"}>
          Sign Up
        </Link>
      </div>
    </div>
  );
};

export default Login;
