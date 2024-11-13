"use client";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { Input } from "../ui/input";
import ImageUpload from "../ui/choose-image";
import { Button } from "../ui/button";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError, Product } from "@/lib/types";
import { useRouter } from "next/navigation";
import { Gender, Season } from "@/lib/constants/enum";
import QuantityForm from "./quantity-form";
import { Separator } from "../ui/separator";
import SizeRadioButton from "../ui/size-button";
import CategoryForm from "./category-form";
import { useEffect, useState } from "react";
import uploadFile from "@/lib/api/uploadFile";
import { ProductSchema } from "./add-new-product";
import { updateProduct } from "@/lib/api/product/updateProduct";

const EditProductContent = ({ product }: { product: Product }) => {
  const router = useRouter();
  const genderList = Object.values(Gender);
  const seasonList = Object.values(Season);
  const [fileList, setFileList] = useState<File[]>([]);
  const [imageList, setImageList] = useState<string[]>([]);

  const {
    control,
    register,
    handleSubmit,
    watch,
    reset,
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
    console.log(files);
  };

  const onSubmit: SubmitHandler<z.infer<typeof ProductSchema>> = async (
    data
  ) => {
    const uploadPromises = fileList.map((file) => uploadFile(file));
    const imageUrls = await Promise.all(uploadPromises);

    if (imageUrls.every((value) => value !== "")) {
      updateProduct(product.id, {
        ...data,
        categories: data.categories.map((item) => item.categoryId),
        quantities: data.quantity.map((item) => {
          return {
            size: item.size,
            color: item.color,
            quantity: item.quantity,
          };
        }),
        images: [...imageList, ...imageUrls],
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
            description: err.response?.data.message ?? "Update product failed",
          });
        });
    }
  };

  useEffect(() => {
    if (product) {
      reset({
        ...product,
        categories: product.categories.map((item) => {
          return { categoryId: item.id, categoryName: item.name };
        }),
        colors: product.colors.map((item) => {
          return { colorName: item.name };
        }),
        sizes: product.sizes.map((item) => {
          return { sizeName: item.name };
        }),
        quantity: Object.entries(product.quantities).map(([key, value]) => {
          const [sizeName, colorName] = key.split("-");
          return {
            size: sizeName,
            color: colorName,
            quantity: value,
          };
        }),
      });
      setImageList(product.images);
    }
  }, [product, reset]);

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
            <ImageUpload
              onUpload={handleUpload}
              imageList={imageList}
              setImageList={setImageList}
            />
          </div>
        </div>
      </div>
      <Separator className="my-10 bg-fs-gray-light" />
      <QuantityForm control={control} watch={watch} />

      <div className="flex mt-16 w-full justify-center gap-4">
        <Button className="sm:w-3/5 w-full min-w-48">Create</Button>
      </div>
    </form>
  );
};

export default EditProductContent;