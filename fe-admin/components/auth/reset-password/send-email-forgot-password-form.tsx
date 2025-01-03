"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import Link from "next/link";
import { useState } from "react";
import { toast } from "@/hooks/use-toast";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import sendEmailForgotPassword from "@/lib/api/reset-password/sendEmailForgotPassword";
import LoadingSpinner from "@/components/ui/loading-spinner";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";

const SendEmailForgotPasswordSchema = z.object({
  email: z.string().email("Invalid email"),
});

const SendEmailForgotPasswordForm = () => {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<z.infer<typeof SendEmailForgotPasswordSchema>>({
    resolver: zodResolver(SendEmailForgotPasswordSchema),
  });

  const onSubmit = async ({ email }: { email: string }) => {
    setIsLoading(true);
    sendEmailForgotPassword({
      email: email,
    })
      .then(() => {
        reset({
          email: "",
        });
        return toast({
          variant: "success",
          title: "Success",
          description:
            "A password reset email has been sent to your email address",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Send email failed",
        });
      });
    setIsLoading(false);
  };

  return (
    <div className="flex flex-col items-center gap-8 flex-1 w-[450px]">
      <div className="flex flex-col gap-2 w-full text-center">
        <h1 className="text-3xl uppercase text-center mb-6">Forgot Password</h1>
        <p className="text-lg text-fs-gray-dark text-center">
          A password reset email will be sent to your email address.
        </p>
      </div>
      <div className="flex flex-col gap-5 p-6 bg-white border shadow-sm w-full">
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
          <div>
            <Input
              id="email"
              {...register("email")}
              placeholder="Email"
            ></Input>
            {errors.email && (
              <span className="error___message">{errors.email.message}</span>
            )}
          </div>
          <Button type="submit" disabled={isLoading} className="uppercase">
            {isLoading ? (
              <LoadingSpinner className={"h-4 w-4 text-white"} />
            ) : (
              "Confirm"
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

export default SendEmailForgotPasswordForm;
