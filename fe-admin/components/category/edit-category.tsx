"use client";
import { useState } from "react";
import { Input } from "../ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { FaPen } from "react-icons/fa";
import { required } from "@/lib/helpers/zod";
import { toast } from "@/hooks/use-toast";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { Button } from "../ui/button";
import { ApiError, Category } from "@/lib/types";
import { AxiosError } from "axios";
import { updateCategory } from "@/lib/api/category/updateCategory";

const CategorySchema = z.object({
  name: required,
});
interface EditCategoryProps {
  onAdded: () => void;
  category: Category;
}
const EditCategoryDialog = ({ onAdded, category }: EditCategoryProps) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<z.infer<typeof CategorySchema>>({
    shouldUnregister: false,
    resolver: zodResolver(CategorySchema),
    defaultValues: category,
  });
  const onSubmit: SubmitHandler<z.infer<typeof CategorySchema>> = async (
    data
  ) => {
    setOpen(false);
    updateCategory(category.id, data)
      .then(() => {
        onAdded();
        return toast({
          variant: "success",
          title: "Success",
          description: "Edit category successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Edit category failed",
        });
      });
  };

  const onErrors: SubmitErrorHandler<z.infer<typeof CategorySchema>> = (
    data
  ) => {
    console.log(data);
    toast({
      variant: "destructive",
      title: "Có lỗi",
      description: "Vui lòng thử lại sau",
    });
  };
  const [open, setOpen] = useState(false);

  return (
    <Dialog
      open={open}
      onOpenChange={(open) => {
        if (open) {
          reset(category);
        }
        setOpen(open);
      }}
    >
      <DialogTrigger asChild>
        <Button
          size="icon"
          variant="outline"
          className="lg:px-4 px-2 whitespace-nowrap rounded-full text-gray-500 hover:text-gray-500"
        >
          <div className="flex flex-wrap gap-1 items-center">
            <FaPen />
          </div>
        </Button>
      </DialogTrigger>
      <DialogContent className="xl:max-w-[720px] max-w-[472px] p-0 bg-white">
        <DialogHeader>
          <DialogTitle className="p-6 pb-0">Edit Category</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit(onSubmit, onErrors)}>
          <div className="p-6 flex flex-col gap-4 border-y-[1px]">
            <div>
              <Input
                placeholder="Name"
                id="nameNcc"
                {...register("name")}
              ></Input>
              {errors.name && (
                <span className="error___message">{errors.name.message}</span>
              )}
            </div>
          </div>
          <div className="p-4 flex-1 flex justify-end">
            <div className="flex gap-4">
              <Button
                type="reset"
                variant={"outline"}
                onClick={() => setOpen(false)}
              >
                Cancel
              </Button>

              <Button type="submit">Edit</Button>
            </div>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default EditCategoryDialog;
