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
import Image from "next/image";
import { toVND } from "@/lib/utils";
import { updateCartItem } from "@/lib/api/cart/updateCartItem";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { useSWRConfig } from "swr";
import { CART_KEY } from "@/hooks/cart/useCartList";
import ColorItem from "../product/color-item";

type DialogProps = {
  cartItem: CartItem;
};

const UpdateColorDialog = ({ cartItem }: DialogProps) => {
  const { mutate } = useSWRConfig();

  const [open, setOpen] = useState(false);
  const [selectedColor, setSelectedColor] = useState(cartItem.color);

  const handleUpdateColor = async () => {
    updateCartItem(cartItem.id, {
      color: selectedColor,
      quantity: cartItem.quantity,
      size: cartItem.size,
    })
      .then(() => {
        toast({
          variant: "success",
          title: "Success",
          description: "Update color successfully",
        });
        mutate(CART_KEY);
        setOpen(false);
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Update color failed",
        });
      });
  };

  useEffect(() => {
    setSelectedColor(cartItem.color);
  }, [cartItem.color]);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger>
        <Button
          type="button"
          variant={"link"}
          className="underline underline-offset-2 text-fs-gray-darker hover:text-fs-gray-darker/80 px-2 py-0 h-fit capitalize"
        >
          {cartItem.color.toLowerCase()}
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
                Size: {cartItem.size.toLowerCase()}
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
                Size: {cartItem.size.toLowerCase()}
              </p>
            </div>
            <div className="flex flex-col mt-auto">
              <span className="font-medium text-base mb-2">Select Color</span>
              <div className="flex gap-4 flex-wrap">
                {cartItem.item.colors.map((color) => (
                  <ColorItem
                    key={color.name}
                    color={color}
                    selected={selectedColor === color.name}
                    disable={
                      cartItem.item.quantities[
                        `${cartItem.size}-${color.name}`
                      ] < 1
                    }
                    onSelected={(value) => {
                      setSelectedColor(value);
                    }}
                  />
                ))}
              </div>
              <Button
                className="rounded-full h-12 mt-4"
                type="button"
                onClick={handleUpdateColor}
                disabled={selectedColor === cartItem.color}
              >
                Update Color
              </Button>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateColorDialog;
