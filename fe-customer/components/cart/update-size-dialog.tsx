"use client";
import { useEffect, useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { ApiError, CartItem } from "@/lib/types";
import { Button } from "../ui/button";
import SizeRadioButton from "../ui/size-button";
import Image from "next/image";
import { toVND } from "@/lib/utils";
import { updateCartItem } from "@/lib/api/cart/updateCartItem";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { useSWRConfig } from "swr";
import { CART_KEY } from "@/hooks/cart/useCartList";

type DialogProps = {
  cartItem: CartItem;
};

const UpdateSizeDialog = ({ cartItem }: DialogProps) => {
  const { mutate } = useSWRConfig();

  const [open, setOpen] = useState(false);
  const [selectedSize, setSelectedSize] = useState(cartItem.size);

  const handleUpdateSize = async () => {
    updateCartItem(cartItem.id, {
      size: selectedSize,
      quantity: cartItem.quantity,
      color: cartItem.color,
    })
      .then(() => {
        toast({
          variant: "success",
          title: "Success",
          description: "Update size successfully",
        });
        mutate(CART_KEY);
        setOpen(false);
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Update size failed",
        });
      });
  };

  useEffect(() => {
    setSelectedSize(cartItem.size);
  }, [cartItem.size]);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger>
        <Button
          type="button"
          variant={"link"}
          className="underline underline-offset-2 text-fs-gray-darker hover:text-fs-gray-darker/80 px-2 py-0 h-fit"
        >
          {cartItem.size}
        </Button>
      </DialogTrigger>
      <DialogContent className="md:left-[50%] md:top-[50%] md:translate-x-[-50%] md:translate-y-[-50%]  rounded-3xl md:rounded-b-3xl rounded-b-none top-auto bottom-0 left-0 right-0 translate-x-0 translate-y-0 md:max-w-3xl max-w-full flex flex-col gap-0 h-fit">
        <DialogHeader>
          <DialogTitle></DialogTitle>
          <DialogDescription></DialogDescription>
        </DialogHeader>
        <div className="flex md:flex-row flex-col gap-4">
          <div className="flex gap-4">
            <Image
              className="md:h-[350px] md:w-[350px] h-40 w-40 object-cover"
              src={cartItem.item.images.at(0) ?? ""}
              alt="prd"
              width={250}
              height={250}
            />
            <div className="md:hidden block">
              <p className="md:text-3xl text-2xl font-medium">
                {cartItem.item.name}
              </p>
              <p className="md:text-xl text-lg font-medium mt-1 mb-2">
                {toVND(cartItem.item.unitPrice)}
              </p>
              <p className="md:text-lg text-base text-fs-gray-darker mt-1 mb-2 capitalize">
                Color: {cartItem.color.toLowerCase()}
              </p>
            </div>
          </div>
          <div className="flex flex-col flex-1">
            <div className="md:block hidden">
              <p className="md:text-3xl text-2xl font-medium">
                {cartItem.item.name}
              </p>
              <p className="md:text-xl text-lg font-medium mt-1 mb-2">
                {toVND(cartItem.item.unitPrice)}
              </p>
              <p className="md:text-lg text-base text-fs-gray-darker mt-1 mb-2 capitalize">
                Color: {cartItem.color.toLowerCase()}
              </p>
            </div>
            <div className="flex flex-col mt-auto">
              <span className="font-medium text-base mb-2">Select Size</span>
              <div className="h-fit grid grid-cols-4 gap-2">
                {cartItem.item.sizes.map((size) => (
                  <SizeRadioButton
                    key={size.name}
                    value={size.name}
                    selected={selectedSize === size.name}
                    className="px-6 flex-1"
                    onSelect={(value) => setSelectedSize(value)}
                    readonly={
                      cartItem.item.quantities[
                        `${size.name}-${cartItem.color}`
                      ] < 1
                    }
                  />
                ))}
              </div>
              <Button
                className="rounded-full h-12 mt-4"
                type="button"
                onClick={handleUpdateSize}
                disabled={selectedSize === cartItem.size}
              >
                Update Size
              </Button>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateSizeDialog;
