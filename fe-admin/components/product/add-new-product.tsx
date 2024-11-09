"use client";
import { required } from "@/lib/helpers/zod";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { SubmitHandler, useForm } from "react-hook-form";
import { Input } from "../ui/input";
import ImageUpload from "../ui/choose-image";
import { Button } from "../ui/button";
import { createProduct } from "@/lib/api/product/createProduct";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { useRouter } from "next/navigation";

const ProductSchema = z.object({
  name: required,
  image: z.string(),
  unitPrice: z.coerce
    .number({ invalid_type_error: "Price must be a number" })
    .gte(1, "Price must larger than zero")
    .refine((value) => Number.isInteger(value), {
      message: "Price must be a whole number",
    }),
  quantity: z.coerce
    .number({ invalid_type_error: "Quantity must be a number" })
    .gte(1, "Quantity must larger than zero")
    .refine((value) => Number.isInteger(value), {
      message: "Quantity must be a whole number",
    }),
});
const AddNewProduct = () => {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<z.infer<typeof ProductSchema>>({
    resolver: zodResolver(ProductSchema),
    defaultValues: {
      name: "",
      image: "",
      unitPrice: 0,
      quantity: 0,
    },
  });

  const onSubmit: SubmitHandler<z.infer<typeof ProductSchema>> = async (
    data
  ) => {
    console.log(data);
    createProduct(data)
      .then(() => {
        router.push("/admin/products")
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
  };
  const handleUpload = (files: File[]) => {
    // You can access the selected files here
    console.log("Uploaded files:", files);
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
          <label className="font-medium mt-7 text-black" htmlFor="name">
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
          <label className="font-medium mt-7 text-black" htmlFor="name">
            Quantity <span className="error___message">*</span>
          </label>
          <div className="mt-2 w-full">
            <Input
              id="quantity"
              {...register("quantity")}
              className="w-full"
            ></Input>
            {errors.quantity && (
              <span className="error___message ml-3">
                {errors.quantity.message}
              </span>
            )}
          </div>
        </div>
        <div className="min-w-72 basis-1/3">
          <label className="font-medium  text-black" htmlFor="name">
            Image <span className="error___message">*</span>
          </label>
          <div className="mt-2">
            <ImageUpload onUpload={handleUpload} />
          </div>
        </div>
      </div>

      <div className="flex mt-16 w-full justify-center gap-4">
        <Button variant={"outline"}>Cancel</Button>
        <Button className="w-40">Create</Button>
      </div>
    </form>
  );
};

export default AddNewProduct;
