/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";
import React, { useCallback, useEffect, useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { required } from "@/lib/helpers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import GenderRadioButton from "@/components/ui/gender-radio-button";
import { stringToDate } from "@/lib/helpers/date";
import { ApiError } from "@/lib/types";
import Image from "next/image";
import ChangeImage from "./change-image";
import { uploadImage } from "@/lib/api/profile/uploadImage";
import { updateUserInfo } from "@/lib/api/profile/updateUserInfo";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { useAuth } from "../auth/auth-context";
import { PiPencilSimpleBold } from "react-icons/pi";
import { RxReset } from "react-icons/rx";
import { format } from "date-fns";
import { vi } from "date-fns/locale";

const ProfileScheme = z.object({
  email: z.string().email("Invalid email"),
  name: required,
  dob: z.coerce.date({
    errorMap: (issue, { defaultError }) => ({
      message: issue.code === "invalid_date" ? "Invalid date" : defaultError,
    }),
  }),
  address: required,
  male: z.boolean(),
});

const ProfileForm = () => {
  const { user, mutate } = useAuth();

  const {
    control,
    register,
    handleSubmit,
    reset,
    setFocus,
    formState: { errors, isDirty },
  } = useForm<z.infer<typeof ProfileScheme>>({
    resolver: zodResolver(ProfileScheme),
    defaultValues: {
      dob: new Date(),
    },
  });

  const [readOnly, setReadOnly] = useState(true);
  const [image, setImage] = useState<any>();

  const handleImageSelected = async () => {
    if (!image) {
      return;
    }
    const formData = new FormData();
    formData.append("file", image);
    uploadImage(formData)
      .then((imgRes) => {
        updateUserInfo({ image: imgRes.data.file })
          .then(() => {
            mutate();
            toast({
              variant: "success",
              title: "Success",
              description: "Update avatar successfully",
            });
          })
          .catch((err: AxiosError<ApiError>) => {
            toast({
              variant: "destructive",
              title: "Error",
              description: err.response?.data.message ?? "Update avatar failed",
            });
          });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Upload avatar failed",
        });
      });
  };

  const onErrors: SubmitErrorHandler<z.infer<typeof ProfileScheme>> = (
    data
  ) => {
    console.log(data);
  };
  const onSubmit: SubmitHandler<z.infer<typeof ProfileScheme>> = async (
    data
  ) => {
    updateUserInfo({
      ...data,
      dob: format(data.dob, "dd/MM/yyyy", {
        locale: vi,
      }),
    })
      .then(() => {
        mutate();
        toast({
          variant: "success",
          title: "Success",
          description: "Update profile successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Update profile failed",
        });
      });
  };

  const resetUser = useCallback(() => {
    if (user) {
      reset({ ...user, dob: stringToDate(user.dob) ?? new Date() });
    }
  }, [user, reset]);

  useEffect(() => {
    resetUser();
  }, [resetUser]);

  if (!user) return <>Skeleton</>;
  return (
    <form
      className="max-w-[800px] w-full"
      onSubmit={handleSubmit(onSubmit, onErrors)}
    >
      <div className="flex flex-col items-center gap-5 ">
        <div className="flex md:gap-16 sm:gap-8 gap-4 w-full">
          <div className="relative h-min mt-20">
            <div className="relative rounded-full border overflow-clip">
              <Image
                src={user.image}
                alt="ảnh"
                className="sm:h-[150px] sm:w-[150px] w-[80px] h-[80px] object-cover"
                width={150}
                height={150}
              ></Image>
            </div>
            <ChangeImage
              image={image}
              setImage={setImage}
              handleImageSelected={handleImageSelected}
              currentImage={user.image}
            />
          </div>
          <div className="flex flex-col gap-5 flex-1">
            <div className="relative flex justify-center gap-2">
              <h1 className="text-3xl mb-6">My Profile</h1>
              {readOnly ? (
                <Button
                  variant={"outline"}
                  size={"icon"}
                  type="button"
                  className="rounded-full"
                  onClick={() => {
                    setReadOnly(false);
                    setFocus("email");
                  }}
                >
                  <PiPencilSimpleBold className="h-6 w-6" />
                </Button>
              ) : (
                <Button
                  variant={"outline"}
                  size={"icon"}
                  type="button"
                  className="rounded-full"
                  onClick={() => {
                    setReadOnly(true);
                    resetUser();
                  }}
                >
                  <RxReset className="h-6 w-6" />
                </Button>
              )}
            </div>
            <div className="w-full">
              <Input
                placeholder="Email address"
                {...register("email")}
                readOnly={readOnly}
              />
              {errors.email && (
                <span className="error___message">{errors.email.message}</span>
              )}
            </div>
            <div className="w-full">
              <Input
                placeholder="Name"
                {...register("name")}
                readOnly={readOnly}
              />
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
                    readonly={readOnly}
                    title="Male"
                    value={true}
                    onSelect={(value) => field.onChange(value)}
                    selected={field.value}
                    className="flex-1"
                  />
                  <GenderRadioButton
                    readonly={readOnly}
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
              <Controller
                control={control}
                name="dob"
                render={({ field }) => (
                  <Input
                    readOnly={readOnly}
                    onClick={(e) => e.preventDefault()}
                    id="dob"
                    value={
                      field.value instanceof Date
                        ? field.value.toISOString().split("T")[0]
                        : field.value || ""
                    }
                    onChange={(e) => field.onChange(e.target.value)}
                    type="date"
                    className="col-span-2 rounded-full"
                  ></Input>
                )}
              />
              {errors.dob && (
                <span className="error___message">{errors.dob.message}</span>
              )}
            </div>
            <div className="w-full">
              <Input
                placeholder="Address"
                {...register("address")}
                readOnly={readOnly}
              />
              {errors.address && (
                <span className="error___message">
                  {errors.address.message}
                </span>
              )}
            </div>
            <Button className="w-full uppercase self-end" disabled={!isDirty}>
              Update
            </Button>
          </div>
        </div>
      </div>
    </form>
  );
};
export default ProfileForm;
