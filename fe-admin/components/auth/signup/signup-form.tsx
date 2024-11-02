"use client";
import React from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { passwordMinError, phoneRegex, required } from "@/lib/helpers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import GenderRadioButton from "@/components/ui/gender-radio-button";

const SignupScheme = z.object({
  email: z.string().email("Invalid email"),
  name: required,
  phone: z.string().regex(phoneRegex, "Invalid phone number"),
  dob: z.coerce.date({
    errorMap: (issue, { defaultError }) => ({
      message: issue.code === "invalid_date" ? "Invalid date" : defaultError,
    }),
  }),
  address: required,
  male: z.boolean(),
  password: z.string().min(6, passwordMinError),
});

const SignupForm = () => {
  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<z.infer<typeof SignupScheme>>({
    resolver: zodResolver(SignupScheme),
  });

  const onSubmit: SubmitHandler<z.infer<typeof SignupScheme>> = async (
    data
  ) => {
    console.log(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-col items-center gap-5 w-[350px]">
        <h1 className="text-3xl uppercase text-center mb-6">Become a member</h1>
        <p className="text-lg text-fs-gray-dark text-center">Create your new Member profile and get access to our products</p>
        <div className="w-full">
          <Input placeholder="Email address" {...register("email")} />
          {errors.email && (
            <span className="error___message">{errors.email.message}</span>
          )}
        </div>
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
        <div className="w-full">
          <Input placeholder="Name" {...register("name")} />
          {errors.name && (
            <span className="error___message">{errors.name.message}</span>
          )}
        </div>
        <Controller
          control={control}
          name="male"
          render={({ field }) => (
            <div className="flex w-full gap-2">
              <GenderRadioButton
                title="Male"
                value={true}
                onSelect={(value) => field.onChange(value)}
                selected={field.value}
                className="flex-1"
              />
              <GenderRadioButton
                title="Female"
                value={false}
                onSelect={(value) => field.onChange(value)}
                selected={!field.value}
                className="flex-1"
              />
            </div>
          )}
        />
        <div className="w-full">
          <Input
            onClick={(e) => e.preventDefault()}
            id="dob"
            type="date"
            {...register("dob")}
          ></Input>
          {errors.dob && (
            <span className="error___message">{errors.dob.message}</span>
          )}
        </div>
        <div className="w-full">
          <Input placeholder="Address" {...register("address")} />
          {errors.address && (
            <span className="error___message">{errors.address.message}</span>
          )}
        </div>
        <Button className="w-full uppercase self-end">SIGN UP</Button>
      </div>
    </form>
  );
};
export default SignupForm;
