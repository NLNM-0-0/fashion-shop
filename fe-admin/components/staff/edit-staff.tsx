"use client";
import { useState } from "react";
import { Input } from "../ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
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
import GenderRadioButton from "../ui/gender-radio-button";
import DaypickerPopup from "../ui/daypicker-popup";
import { format } from "date-fns";
import { vi } from "date-fns/locale";
import { stringToDate } from "@/lib/helpers/date";
import { ApiError, Staff } from "@/lib/types";
import { updateStaff } from "@/lib/api/staff/updateStaff";
import { AxiosError } from "axios";

const StaffSchema = z.object({
  name: required,
  email: z.string().email("Email không hợp lệ"),
  dob: required,
  male: z.boolean(),
  address: required,
  image: z.string(),
  admin: z.boolean(),
});
interface EditStaffProps {
  onAdded: () => void;
  staff: Staff;
}
const EditStaffDialog = ({ onAdded, staff }: EditStaffProps) => {
  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<z.infer<typeof StaffSchema>>({
    shouldUnregister: false,
    resolver: zodResolver(StaffSchema),
    defaultValues: staff,
  });
  const onSubmit: SubmitHandler<z.infer<typeof StaffSchema>> = async (data) => {
    setOpen(false);
    updateStaff(staff.id, data)
      .then(() => {
        onAdded();
        return toast({
          variant: "success",
          title: "Success",
          description: "Edit staff successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Edit staff failed",
        });
      });
  };

  const onErrors: SubmitErrorHandler<z.infer<typeof StaffSchema>> = (data) => {
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
          reset(staff);
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
          <DialogTitle className="p-6 pb-0">Edit Staff</DialogTitle>
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
            <div>
              <Input
                id="email"
                placeholder="Email"
                {...register("email")}
              ></Input>
              {errors.email && (
                <span className="error___message">{errors.email.message}</span>
              )}
            </div>
            <div>
              <Input
                id="address"
                placeholder="Address"
                {...register("address")}
              ></Input>
              {errors.address && (
                <span className="error___message">
                  {errors.address.message}
                </span>
              )}
            </div>
            <div>
              <Controller
                control={control}
                name={`dob`}
                render={({ field }) => {
                  const dateObject = stringToDate(field.value);
                  return (
                    <DaypickerPopup
                      triggerClassname="flex-1 w-full"
                      date={dateObject ?? new Date()}
                      setDate={(date) =>
                        field.onChange(
                          format(date!, "dd/MM/yyyy", {
                            locale: vi,
                          })
                        )
                      }
                    />
                  );
                }}
              />
              {errors.dob && (
                <span className="error___message ml-3">
                  {errors.dob.message}
                </span>
              )}
            </div>
            <Controller
              control={control}
              name={`male`}
              render={({ field }) => (
                <div className="flex flex-1 gap-2">
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

            <Controller
              control={control}
              name={`admin`}
              render={({ field }) => (
                <div className="flex flex-1 gap-2">
                  <GenderRadioButton
                    title="Admin"
                    value={true}
                    onSelect={(value) => field.onChange(value)}
                    selected={field.value}
                    className="flex-1"
                  />
                  <GenderRadioButton
                    title="Normal Staff"
                    value={false}
                    onSelect={(value) => field.onChange(value)}
                    selected={!field.value}
                    className="flex-1"
                  />
                </div>
              )}
            />
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

export default EditStaffDialog;
