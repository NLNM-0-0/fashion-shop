"use client";
import React, { useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { passwordMinError, phoneRegex } from "@/lib/helpers/zod";
import { AxiosError } from "axios";
import { ApiError, ForgotPasswordPayload, VerifyOtpPayload } from "@/lib/types";
import { toast } from "@/hooks/use-toast";
import { verifyOTP } from "@/lib/api/auth/verifyOTP";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { sendOTP } from "@/lib/api/auth/sendOTP";
import useCountDown from "@/hooks/useCountDown";
import { addSeconds, format } from "date-fns";
import { forgotPassword } from "@/lib/api/auth/forgotPassword";

const ForgotPasswordScheme = z.object({
  phone: z.string().regex(phoneRegex, "Invalid phone number"),
  otp: z
    .string()
    .min(6, "The 6-digit OTP you entered is invalid")
    .max(6, "The 6-digit OTP you entered is invalid"),
  password: z.string().min(6, passwordMinError),
});

const ForgotPasswordForm = () => {
  const {
    control,
    handleSubmit,
    register,
    getValues,
    watch,
    formState: { errors, isDirty, isValid },
  } = useForm<z.infer<typeof ForgotPasswordScheme>>({
    resolver: zodResolver(ForgotPasswordScheme),
    mode: "onChange",
    defaultValues: { phone: "", otp: "", password: "" },
  });
  const { remainingTime, startCountdown, wasOptSend } = useCountDown();

  const [verified, setVerified] = useState(false);

  const otpValue = watch("otp");

  const handleSendOtp = async () => {
    sendOTP({ phone: getValues().phone })
      .then((res) => {
        if (res.data.data) {
          startCountdown();
        }
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Send OTP failed",
        });
      });
  };

  const handleVerifyOtp = async (data: VerifyOtpPayload) => {
    verifyOTP(data)
      .then((res) => {
        if (res.data.data) {
          setVerified(true);
        }
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Verify OTP failed",
        });
      });
  };
  const onSubmit = async (data: ForgotPasswordPayload) => {
    forgotPassword(data)
      .then((res) => {
        if (res.data.data) {
          window.location.href = "/login";
        }
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Reset password failed.",
        });
      });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-col items-center gap-5 w-[350px]">
        <h1 className="text-3xl uppercase text-center mb-6">
          Verify Your Account
        </h1>
        {verified ? (
          <div className="flex-1 w-full">
            <Input
              placeholder="New password"
              type="password"
              {...register("password")}
            />
            {errors.password && (
              <span className="error___message">{errors.password.message}</span>
            )}
          </div>
        ) : (
          <>
            <div className="w-full flex flex-col gap-2">
              <span className="text-fs-gray-dark text-right">
                {!!remainingTime &&
                  format(addSeconds(new Date(0), remainingTime), "mm:ss")}
              </span>
              <div className="relative flex-1">
                <Input
                  placeholder="Phone number"
                  {...register("phone")}
                  readOnly={wasOptSend}
                />
                {errors.phone && (
                  <span className="error___message">
                    {errors.phone.message}
                  </span>
                )}
                <Button
                  disabled={!isDirty || !!errors.phone || wasOptSend}
                  type="button"
                  variant={"outline"}
                  className="absolute top-1 right-1 h-8 px-2"
                  onClick={() => handleSendOtp()}
                >
                  <span className="text-sm">Send OTP</span>
                </Button>
              </div>
            </div>
            <div className="w-full flex justify-center">
              <Controller
                control={control}
                name="otp"
                render={({ field }) => (
                  <InputOTP
                    disabled={!wasOptSend}
                    maxLength={6}
                    onChange={(value) => field.onChange(value)}
                  >
                    <InputOTPGroup>
                      <InputOTPSlot index={0} />
                      <InputOTPSlot index={1} />
                      <InputOTPSlot index={2} />
                    </InputOTPGroup>
                    <InputOTPSeparator />
                    <InputOTPGroup>
                      <InputOTPSlot index={3} />
                      <InputOTPSlot index={4} />
                      <InputOTPSlot index={5} />
                    </InputOTPGroup>
                  </InputOTP>
                )}
              />
            </div>
          </>
        )}
        {verified ? (
          <Button
            type="submit"
            className="w-full"
            disabled={!isDirty || !isValid}
          >
            Reset Password
          </Button>
        ) : (
          <Button
            type="button"
            className="w-full"
            onClick={() =>
              handleVerifyOtp({ phone: getValues().phone, otp: otpValue })
            }
            disabled={
              !isDirty ||
              !otpValue ||
              otpValue.length !== 6 ||
              !!errors.phone ||
              !wasOptSend
            }
          >
            Verify OTP
          </Button>
        )}
      </div>
    </form>
  );
};
export default ForgotPasswordForm;
