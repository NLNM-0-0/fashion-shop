import LoginForm from "@/components/auth/login/login-form";
import { Metadata } from "next";
import Image from "next/image";

export const metadata: Metadata = {
  title: "Login",
};
const Login = () => {
  return (
    <div className="flex flex-col w-full h-full py-20 items-center">
      <Image
        src={"/android-chrome-192x192.png"}
        alt="logo"
        width={100}
        height={100}
      />
      <LoginForm />
    </div>
  );
};

export default Login;
