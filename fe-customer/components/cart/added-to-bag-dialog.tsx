"use client";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "../ui/dialog";
import { CartItemDetail } from "@/lib/types";
import CheckCircle from "@/lib/assets/icons/check-circle.svg";
import { Button, buttonVariants } from "../ui/button";
import Link from "next/link";
import { cn } from "@/lib/utils";
import AddedItem from "./added-item";
import Image from "next/image";

export interface AddedItemProps {
  item: CartItemDetail;
  size: string;
  color: string;
}

type DialogProps = {
  product: AddedItemProps;
  open: boolean;
  onOpenChange: (open: boolean) => void;
};
const AddedToBagDialog = ({ product, open, onOpenChange }: DialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="rounded-2xl top-20 left-auto right-6 translate-x-0 translate-y-0 max-w-lg w-[348px] flex flex-col md:data-[state=closed]:slide-out-to-right-1/2 md:data-[state=closed]:slide-out-to-top-[48%] md:data-[state=open]:slide-in-from-right-1/2 md:data-[state=open]:slide-in-from-top-[48%] data-[state=closed]:slide-out-to-right-1/2 data-[state=closed]:slide-out-to-top-[48%] data-[state=open]:slide-in-from-right-1/2 data-[state=open]:slide-in-from-top-[48%]">
        <DialogHeader>
          <DialogTitle className="flex gap-5">
            <Image alt="check" height={20} width={20} src={CheckCircle.src} />
            Added to Bag
          </DialogTitle>
          <DialogDescription></DialogDescription>
        </DialogHeader>
        <div className="w-full h-full flex flex-col gap-2">
          <AddedItem product={product} />
          <Link
            href={"/admin/cart"}
            className={cn(
              buttonVariants({ variant: "outline" }),
              "rounded-full h-12"
            )}
          >
            View Bag
          </Link>
          <Button type="button" className="rounded-full h-12">
            Checkout
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default AddedToBagDialog;
