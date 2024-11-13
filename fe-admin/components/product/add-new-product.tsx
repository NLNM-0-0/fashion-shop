"use client";
import { required } from "@/lib/helpers/zod";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { Input } from "../ui/input";
import ImageUpload from "../ui/choose-image";
import { Button } from "../ui/button";
import { createProduct } from "@/lib/api/product/createProduct";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { useRouter } from "next/navigation";
import { Gender, Season } from "@/lib/constants/enum";
import QuantityForm from "./quantity-form";
import { Separator } from "../ui/separator";
import SizeRadioButton from "../ui/size-button";
import CategoryForm from "./category-form";
import { useState } from "react";
import uploadFile from "@/lib/api/uploadFile";

export const ProductSchema = z.object({
  name: required,
  unitPrice: z.coerce
    .number({ invalid_type_error: "Price must be a number" })
    .gte(1, "Price must larger than zero")
    .refine((value) => Number.isInteger(value), {
      message: "Price must be a whole number",
    }),
  quantity: z.array(
    z.object({
      quantityKey: z.string(),
      size: z.string(),
      color: z.string(),
      quantity: z.coerce.number(),
    })
  ),
  colors: z.array(z.object({ colorName: z.string() })),
  sizes: z.array(z.object({ sizeName: z.string() })),
  categories: z.array(
    z.object({ categoryId: z.coerce.number(), categoryName: z.string() })
  ),
  gender: required,
  season: required,
});
const AddNewProduct = () => {
  const router = useRouter();

  const genderList = Object.values(Gender);
  const seasonList = Object.values(Season);
  const [fileList, setFileList] = useState<File[]>([]);

  const {
    control,
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<z.infer<typeof ProductSchema>>({
    resolver: zodResolver(ProductSchema),
    defaultValues: {
      name: "",
      unitPrice: 0,
      quantity: [],
      colors: [],
      sizes: [],
      categories: [],
      gender: Gender.MEN,
      season: Season.SPRING,
    },
  });

  const handleUpload = (files: File[]) => {
    setFileList(files);
  };

  const onSubmit: SubmitHandler<z.infer<typeof ProductSchema>> = async (
    data
  ) => {
    const uploadPromises = fileList.map((file) => uploadFile(file));
    const imageUrls = await Promise.all(uploadPromises);

    if (imageUrls.every((value) => value !== "")) {
      createProduct({
        ...data,
        categories: data.categories.map((item) => item.categoryId),
        quantities: data.quantity.map((item) => {
          return {
            size: item.size,
            color: item.color,
            quantity: item.quantity,
          };
        }),
        images: imageUrls,
      })
        .then(() => {
          router.push("/admin/products");
          toast({
            variant: "success",
            title: "Success",
            description: "Create product successfully",
          });
        })
        .catch((err: AxiosError<ApiError>) => {
          toast({
            variant: "destructive",
            title: "Error",
            description: err.response?.data.message ?? "Create product failed",
          });
        });
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="card___style flex flex-col w-full py-12"
    >
      <div className="flex lg:flex-row flex-col gap-10">
        <div className="flex flex-1 flex-col text-gray-text">
          <label className="font-medium text-black" htmlFor="name">
            Product Name <span className="error___message">*</span>
          </label>
          <div className="mt-2 w-full">
            <Input id="name" {...register("name")} className="w-full"></Input>
            {errors.name && (
              <span className="error___message ml-3">
                {errors.name.message}
              </span>
            )}
          </div>
          <label className="font-medium mt-7 text-black" htmlFor="unitPrice">
            Unit Price <span className="error___message">*</span>
          </label>
          <div className="mt-2 w-full">
            <Input
              id="unitPrice"
              {...register("unitPrice")}
              className="w-full"
            ></Input>
            {errors.unitPrice && (
              <span className="error___message ml-3">
                {errors.unitPrice.message}
              </span>
            )}
          </div>
          <div className="flex flex-1 gap-5 md:flex-row flex-col-reverse">
            <div className="flex-1">
              <div className="font-medium mt-7 text-black mb-2">
                Gender
                <span className="error___message"> *</span>
              </div>
              <Controller
                control={control}
                name="gender"
                render={({ field }) => (
                  <div className="flex flex-wrap gap-2">
                    {genderList.map((gender) => (
                      <SizeRadioButton
                        key={gender}
                        name={gender}
                        selected={field.value === gender}
                        onSelect={(value) => field.onChange(value)}
                      />
                    ))}
                  </div>
                )}
              />
              <div className="font-medium mt-7 text-black mb-2">
                Season
                <span className="error___message"> *</span>
              </div>
              <Controller
                control={control}
                name="season"
                render={({ field }) => (
                  <div className="flex gap-2 flex-wrap">
                    {seasonList.map((season) => (
                      <SizeRadioButton
                        key={season}
                        name={season}
                        selected={field.value === season}
                        onSelect={(value) => field.onChange(value)}
                      />
                    ))}
                  </div>
                )}
              />
            </div>
            <div className="flex-1">
              <div className="font-medium mt-7 text-black mb-2">
                Category
                <span className="error___message"> *</span>
              </div>
              <CategoryForm control={control} />
            </div>
          </div>
        </div>
        <div className="min-w-72 basis-1/3">
          <label className="font-medium  text-black">
            Image{" "}
            <span className="text-fs-gray-dark text-sm font-normal">
              {" "}
              (Maximum 10){" "}
            </span>
            <span className="error___message">*</span>
          </label>
          <div className="mt-2">
            <ImageUpload onUpload={handleUpload} />
          </div>
        </div>
      </div>
      <Separator className="my-10 bg-fs-gray-light" />
      <QuantityForm control={control} watch={watch} />

      <Button className="w-52">Create</Button>
    </form>
  );
};

export default AddNewProduct;
