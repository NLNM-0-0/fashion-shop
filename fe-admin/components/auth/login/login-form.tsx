"use client";
import React from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { login } from "@/lib/api/auth/login";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { passwordMinError } from "@/lib/helpers/zod";
import Link from "next/link";

const LoginScheme = z.object({
  email: z.string().email("Invalid email"),
  password: z.string().min(6, passwordMinError),
});

const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<z.infer<typeof LoginScheme>>({
    resolver: zodResolver(LoginScheme),
  });

  const onSubmit = async ({
    email,
    password,
  }: {
    email: string;
    password: string;
  }) => {
    login({ email, password })
      .then(() => {
        window.location.href = "/admin";
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-col items-center gap-5 w-[350px]">
        <h1 className="text-3xl uppercase text-center mb-6">
          Manage Your Brand, Your Way
        </h1>
        <div className="w-full">
          <Input placeholder="Email address" {...register("email")} />
          {errors.email && (
            <span className="error___message">{errors.email.message}</span>
          )}
        </div>
        <div className="w-full">
          <Input
            type="password"
            placeholder="Password"
            {...register("password")}
          />
          {errors.password && (
            <span className="error___message">{errors.password.message}</span>
          )}
        </div>
        <Link
          className="text-sm text-fs-gray-dark self-end"
          href={"/reset-password"}
        >
          Forgotten your password?
        </Link>
        <Button className="w-full">SIGN IN</Button>
      </div>
    </form>
  );
};
export default LoginForm;
