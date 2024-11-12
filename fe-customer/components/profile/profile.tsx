import React from "react";
import { Button } from "../ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";
import { useEffect, useState } from "react";
import { LuLogOut } from "react-icons/lu";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { Input } from "../ui/input";
import { z } from "zod";
import { SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { MdOutlineLock } from "react-icons/md";
import { passwordMinError, required } from "@/lib/helpers/zod";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { useAuth } from "../auth/auth-context";
import { changePassword } from "@/lib/api/auth/changePassword";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import ChangeAvatar from "./change-avatar";
import { GoBell } from "react-icons/go";
import { useRouter } from "next/navigation";
import { useUnseenNumber } from "@/hooks/useUnseenNumber";

const PasswordSchema = z
  .object({
    oldPassword: z.string().min(6, passwordMinError),
    newPassword: z.string().min(6, passwordMinError),
    confirmNewPass: required,
  })
  .refine((data) => data.newPassword === data.confirmNewPass, {
    message: "Passwords do not match",
    path: ["confirmNewPass"],
  });

const Profile = () => {
  const { user, logout, mutate } = useAuth();
  const { data: unseenNoti } = useUnseenNumber();
  const router = useRouter();

  const [open, setOpen] = useState(false);
  const [openPass, setOpenPass] = useState(false);
  const [avatar, setAvatar] = useState("");

  const passForm = useForm<z.infer<typeof PasswordSchema>>({
    resolver: zodResolver(PasswordSchema),
    defaultValues: {
      oldPassword: "",
      newPassword: "",
      confirmNewPass: "",
    },
  });
  const {
    register: registerPass,
    handleSubmit: handleSubmitPass,
    reset: resetPass,
    formState: { errors: errorPass },
  } = passForm;

  const onSubmitPass: SubmitHandler<z.infer<typeof PasswordSchema>> = async (
    data
  ) => {
    changePassword({
      oldPassword: data.oldPassword,
      newPassword: data.newPassword,
    })
      .then(() => {
        setOpen(false);
        setOpenPass(false);
        toast({
          variant: "success",
          title: "Sucess",
          description: "Change password successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Change password failed",
        });
      });
  };

  const onOpenPass = (value: boolean) => {
    if (value) {
      resetPass();
    }
    setOpenPass(value);
  };

  useEffect(() => {
    if (user) {
      setAvatar(user.image);
    }
  }, [user]);

  return (
    <div>
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <div className="flex items-center">
            <div className="relative">
              <Avatar className="cursor-pointer rounded-full border overflow-clip">
                <AvatarImage src={avatar} alt="avatar" />
                <AvatarFallback>{user?.name.substring(0, 2)}</AvatarFallback>
              </Avatar>
              {unseenNoti && unseenNoti.data.number > 0 && (
                <div className="absolute top-0 right-[1px] rounded-full bg-sky-600 h-2.5 w-2.5" />
              )}
            </div>
          </div>
        </PopoverTrigger>
        <PopoverContent className="w-48 mx-4 flex flex-col gap-2 p-0 py-2">
          <div className="flex flex-col px-4 pt-2">
            <h1 className="text-lg font-semibold">{user?.name}</h1>
          </div>
          <ChangeAvatar
            onChanged={() => void mutate()}
            currentImage={user?.image || ""}
          />
          <Button
            variant={"ghost"}
            className="rounded-none w-full justify-start"
            onClick={() => {
              router.push("/fa/notifications");
              setOpen(false);
            }}
          >
            <div className="flex gap-2 items-center text-primary">
              <div className="relative">
                <GoBell className="w-6 h-6" />
                {unseenNoti && unseenNoti.data.number != 0 && (
                  <div className="absolute top-[-8px] right-[-4px] rounded-full bg-sky-600 h-4 w-4 flex leading-3 justify-center items-center text-xs font-medium text-accent">
                    {unseenNoti.data.number}
                  </div>
                )}
              </div>
              Notifications
            </div>
          </Button>

          <Dialog open={openPass} onOpenChange={onOpenPass}>
            <DialogTrigger asChild>
              <Button variant={"ghost"} className="rounded-none justify-start">
                <div className="flex gap-2 items-center">
                  <MdOutlineLock className="h-6 w-6" />
                  Change Password
                </div>
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-[472px] p-0 bg-white">
              <DialogHeader>
                <DialogTitle className="p-6 pb-0">Change Password</DialogTitle>
              </DialogHeader>
              <form onSubmit={handleSubmitPass(onSubmitPass)}>
                <div className="p-6 flex flex-col gap-4 border-y-[1px]">
                  <div>
                    <Input
                      placeholder="Current password"
                      type="password"
                      id="pass"
                      {...registerPass("oldPassword")}
                    ></Input>
                    {errorPass.oldPassword && (
                      <span className="error___message">
                        {errorPass.oldPassword.message}
                      </span>
                    )}
                  </div>
                  <div>
                    <Input
                      type="password"
                      placeholder="New password"
                      id="new"
                      {...registerPass("newPassword")}
                    ></Input>
                    {errorPass.newPassword && (
                      <span className="error___message">
                        {errorPass.newPassword.message}
                      </span>
                    )}
                  </div>
                  <div>
                    <Input
                      type="password"
                      placeholder="Confirm new password"
                      id="confirm"
                      {...registerPass("confirmNewPass")}
                    ></Input>
                    {errorPass.confirmNewPass && (
                      <span className="error___message">
                        {errorPass.confirmNewPass.message}
                      </span>
                    )}
                  </div>
                </div>
                <div className="p-4 flex-1 flex justify-end">
                  <div className="flex gap-4">
                    <Button
                      type="button"
                      variant={"outline"}
                      onClick={() => setOpenPass(false)}
                    >
                      Cancel
                    </Button>

                    <Button>Confirm</Button>
                  </div>
                </div>
              </form>
            </DialogContent>
          </Dialog>
          <form action={logout} className="w-full">
            <Button
              variant={"ghost"}
              className="rounded-none w-full justify-start"
            >
              <div className="flex items-center gap-2 text-primary">
                <LuLogOut className="h-6 w-6" />
                Log out
              </div>
            </Button>
          </form>
        </PopoverContent>
      </Popover>
    </div>
  );
};

export default Profile;
