"use client";
import React from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { login } from "@/lib/api/auth/login";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { passwordMinError, phoneRegex } from "@/lib/helpers/zod";
import Link from "next/link";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { toast } from "@/hooks/use-toast";

const LoginScheme = z.object({
  phone: z.string().regex(phoneRegex, "Invalid phone number"),
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
    phone,
    password,
  }: {
    phone: string;
    password: string;
  }) => {
    login({ phone, password })
      .then(() => {
        window.location.href = "/fa";
      })
      .catch((err: AxiosError<ApiError>) => {
        if (err.response?.status === 403) {
          window.location.href = "/otp";
        } else {
          toast({
            variant: "destructive",
            title: "Error",
            description: err.response?.data.message ?? "Login failed",
          });
        }
      });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-col items-center gap-5 w-[350px]">
        <h1 className="text-3xl uppercase text-center mb-6">
          Your Account, Your Way
        </h1>
        <div className="w-full">
          <Input placeholder="Phone number" {...register("phone")} />
          {errors.phone && (
            <span className="error___message">{errors.phone.message}</span>
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
        <Button type="submit" className="w-full">
          SIGN IN
        </Button>
      </div>
    </form>
  );
};
export default LoginForm;
