"use client";

import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { z } from "zod";
import { SubmitHandler, useFieldArray, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Textarea } from "../ui/textarea";
import { AiOutlineClose } from "react-icons/ai";
import { useSWRConfig } from "swr";
import { toast } from "@/hooks/use-toast";
import StaffList from "../staff/staff-filter-list";
import { createNotification } from "@/lib/api/notification/createNotification";
import { required } from "@/lib/helpers/zod";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { UNSEEN_KEY } from "@/hooks/useUnseenNumber";

const Schema = z.object({
  title: required,
  description: z.string().max(200, "Maximum 200 characters"),
  receivers: z.array(
    z.object({ userId: z.coerce.number(), userName: z.string() })
  ),
});
const CreateNotification = ({ onCreated }: { onCreated?: () => void }) => {
  const { mutate } = useSWRConfig();

  const [open, setOpen] = useState(false);
  const [staff] = useState(-1);

  const {
    register,
    handleSubmit,
    reset,
    control,
    formState: { errors },
  } = useForm<z.infer<typeof Schema>>({
    shouldUnregister: false,
    resolver: zodResolver(Schema),
    defaultValues: {
      title: "",
      description: "",
      receivers: [],
    },
  });
  const { fields, append, remove } = useFieldArray({
    control: control,
    name: "receivers",
  });

  const onSelect = (userId: number, name: string) => {
    const selectedIndex = fields.findIndex(
      (feature) => feature.userId === userId
    );
    if (selectedIndex > -1) {
      return;
    } else {
      append({ userId: userId, userName: name });
    }
  };

  const onSubmit: SubmitHandler<z.infer<typeof Schema>> = async (data) => {
    createNotification({
      title: data.title.trim(),
      description: data.description.trim(),
      receiver: data.receivers.map((item) => item.userId),
    })
      .then(() => {
        toast({
          variant: "success",
          title: "Success",
          description: "Send notifications successfully",
        });
        if (onCreated) {
          onCreated();
        }
        reset({
          title: "",
          description: "",
          receivers: [],
        });
        mutate(UNSEEN_KEY);
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description:
            err.response?.data.message ?? "Create notification failed",
        });
      });
  };
  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button>Send notification</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle className="pb-2"> Send notification</DialogTitle>
        </DialogHeader>
        <form className="flex flex-col" onSubmit={handleSubmit(onSubmit)}>
          <label className="font-medium mt-2 text-black" htmlFor="title">
            Title <span className="error___message">*</span>
          </label>
          <div className="col-span-2">
            <Input id="title" {...register("title")}></Input>
            {errors.title && (
              <span className="error___message ml-3">
                {errors.title.message}
              </span>
            )}
          </div>
          <label className="font-medium mt-7 text-black" htmlFor="desc">
            Content
          </label>
          <Textarea id="desc" {...register("description")} maxLength={200} />
          {errors.title && (
            <span className="error___message ml-3">{errors.title.message}</span>
          )}
          <div className="font-medium mt-7 text-black">
            Recipient{" "}
            <span className="font-normal text-gray-text">
              (Leave blank to send to all)
            </span>
          </div>
          <div className="flex gap-2 my-2">
            {fields.map((item, index) => {
              return (
                <div
                  key={item.userId}
                  className="rounded-xl flex self-start px-3 pr-1 py-1 h-fit outline-none text-sm text-primary bg-primary/10 items-center gap-1 whitespace-nowrap"
                >
                  <p>
                    {item.userName && item.userName !== ""
                      ? item.userName
                      : item.userId}
                  </p>
                  <AiOutlineClose
                    className="text-primary h-6 w-auto cursor-pointer p-1 px-2 hover:text-rose-500"
                    onClick={() => {
                      remove(index);
                    }}
                  />
                </div>
              );
            })}
          </div>
          <StaffList
            isId
            staff={staff}
            setStaff={(value: string | number, name: string | undefined) =>
              onSelect(+value, name ?? "")
            }
          />

          <div className="flex gap-5 justify-end sm:self-end mt-7">
            <Button
              variant={"outline"}
              type="button"
              onClick={() => {
                setOpen(false);
              }}
              className="sm:flex flex-1 w-auto"
            >
              Cancel
            </Button>
            <Button
              type="submit"
              className="sm:flex flex-1 whitespace-nowrap"
              onClick={() => {
                setOpen(false);
              }}
            >
              Confirm
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default CreateNotification;
