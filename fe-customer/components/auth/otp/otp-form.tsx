"use client";
import React from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { phoneRegex } from "@/lib/helpers/zod";
import { AxiosError } from "axios";
import { ApiError, VerifyOtpPayload } from "@/lib/types";
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

const VerifyOTPScheme = z.object({
  phone: z.string().regex(phoneRegex, "Invalid phone number"),
  otp: z
    .string()
    .min(6, "The 6-digit OTP you entered is invalid")
    .max(6, "The 6-digit OTP you entered is invalid"),
});

const VerifyOTPForm = () => {
  const {
    control,
    register,
    handleSubmit,
    getValues,
    formState: { errors, isDirty, isValid },
  } = useForm<z.infer<typeof VerifyOTPScheme>>({
    resolver: zodResolver(VerifyOTPScheme),
    mode: "onChange",
    defaultValues: { phone: "", otp: "" },
  });
  const { remainingTime, startCountdown, wasOptSend } = useCountDown();

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

  const onSubmit = async (data: VerifyOtpPayload) => {
    verifyOTP(data)
      .then((res) => {
        if (res.data.data) {
          window.location.href = "/login";
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

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-col items-center gap-5 w-[350px]">
        <h1 className="text-3xl uppercase text-center mb-6">
          Verify Your Account
        </h1>
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
              <span className="error___message">{errors.phone.message}</span>
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
        <Button
          type="submit"
          className="w-full"
          disabled={!isDirty || !isValid || !wasOptSend}
        >
          Verify
        </Button>
      </div>
    </form>
  );
};
export default VerifyOTPForm;
