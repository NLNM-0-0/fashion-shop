"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import Link from "next/link";
import { passwordMinError, required } from "@/lib/helpers/zod";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { toast } from "@/hooks/use-toast";
import verifyForgotPassword from "@/lib/api/reset-password/verifyForgotPassword";
import { ChangePasswordProps } from "@/app/(auth)/reset-password/[token]/page";
import LoadingSpinner from "@/components/ui/loading-spinner";

const ChangePasswordFormSchema = z
  .object({
    password: z.string().min(6, passwordMinError),
    confirmNewPass: required,
  })
  .refine((data) => data.password === data.confirmNewPass, {
    message: "Passwords do not match",
    path: ["confirmNewPass"],
  });

const ChangePasswordForm = (props: ChangePasswordProps) => {
  const { token } = props;
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const {
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<z.infer<typeof ChangePasswordFormSchema>>({
    resolver: zodResolver(ChangePasswordFormSchema),
  });

  const onSubmit = async ({ password }: { password: string }) => {
    setIsLoading(true);
    const responseData = await verifyForgotPassword({
      forgetPasswordToken: token,
      password: password,
    });
    if (responseData.hasOwnProperty("data")) {
      if (responseData.data === true) {
        toast({
          variant: "success",
          title: "Success",
          description: "Change password successfully",
        });
        setIsLoading(false);
        reset({
          password: "",
          confirmNewPass: "",
        });
      }
    } else if (responseData.hasOwnProperty("message")) {
      toast({
        variant: "destructive",
        title: "Error",
        description: responseData.message,
      });
    } else {
      toast({
        variant: "destructive",
        title: "Error",
        description: "Try again",
      });
    }
    setIsLoading(false);
  };

  return (
    <div className="flex flex-col gap-8 flex-1 w-[450px]">
      <div className="flex flex-col gap-2">
        <h1 className="text-3xl uppercase text-center mb-6">Forgot Password</h1>
        <p className="text-lg text-fs-gray-dark text-center">
          Enter your new password
        </p>
      </div>
      <div className="flex flex-col gap-4 p-6 bg-white border shadow-md">
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
          <div>
            <Input
              type="password"
              id="new"
              {...register("password")}
              placeholder="New password"
            ></Input>
            {errors.password && (
              <span className="error___message">{errors.password.message}</span>
            )}
          </div>
          <div>
            {/* <Label htmlFor="new">Xác nhận mật khẩu mới</Label> */}
            <Input
              type="password"
              id="confirm"
              {...register("confirmNewPass")}
              placeholder="Confirm new password"
            ></Input>
            {errors.confirmNewPass && (
              <span className="error___message">
                {errors.confirmNewPass.message}
              </span>
            )}
          </div>
          <Button type="submit" disabled={isLoading}>
            {isLoading ? (
              <LoadingSpinner className={"h-4 w-4 text-white"} />
            ) : (
              "Change password"
            )}
          </Button>
        </form>
        <Link href={"/login"} className="self-center">
          <p className="text-fs-gray-dark text-sm">Back to login</p>
        </Link>
      </div>
    </div>
  );
};

export default ChangePasswordForm;
