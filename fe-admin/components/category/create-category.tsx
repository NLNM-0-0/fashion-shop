"use client";
import { useState } from "react";
import { Input } from "../ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { SubmitHandler, useForm } from "react-hook-form";
import { FaPlus } from "react-icons/fa";
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
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { createCategory } from "@/lib/api/category/createCategory";

const CategorySchema = z.object({
  name: required,
});
interface CreateCategoryProps {
  onAdded: () => void;
}
const CreateCategoryDialog = ({ onAdded }: CreateCategoryProps) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<z.infer<typeof CategorySchema>>({
    shouldUnregister: false,
    resolver: zodResolver(CategorySchema),
    defaultValues: {
      name: "",
    },
  });
  const onSubmit: SubmitHandler<z.infer<typeof CategorySchema>> = async (
    data
  ) => {
    setOpen(false);
    createCategory(data)
      .then(() => {
        onAdded();
        return toast({
          variant: "success",
          title: "Success",
          description: "Add category successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Add category failed",
        });
      });
  };
  const [open, setOpen] = useState(false);

  return (
    <Dialog
      open={open}
      onOpenChange={(open) => {
        if (open) {
          reset({
            name: "",
          });
        }
        setOpen(open);
      }}
    >
      <DialogTrigger asChild>
        <Button className="px-4 whitespace-nowrap">
          <div className="flex flex-wrap gap-1 items-center">
            <FaPlus />
            Add Category
          </div>
        </Button>
      </DialogTrigger>
      <DialogContent className="xl:max-w-[720px] max-w-[472px] p-0 bg-white">
        <DialogHeader>
          <DialogTitle className="p-6 pb-0">Add Category</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit(onSubmit)}>
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

              <Button type="submit">Add</Button>
            </div>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default CreateCategoryDialog;
