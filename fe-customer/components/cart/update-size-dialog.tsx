"use client";
import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";

type DialogProps = {
  children: React.ReactNode;
  trigger: React.ReactNode;
};
const UpdateSizeDialog = ({ children, trigger }: DialogProps) => {
  const [open, setOpen] = useState(false);
  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>{trigger}</DialogTrigger>
      <DialogContent className="md:left-[50%] md:top-[50%] md:translate-x-[-50%] md:translate-y-[-50%]  rounded-3xl md:rounded-b-3xl rounded-b-none bottom-0 left-0 right-0 translate-x-0 translate-y-0 md:max-w-lg max-w-full flex flex-col">
        <DialogHeader>
          <DialogTitle></DialogTitle>
          <DialogDescription></DialogDescription>
        </DialogHeader>
        <div className="w-full h-full">{children}</div>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateSizeDialog;
