import { LuMinus, LuPlus, LuTrash2 } from "react-icons/lu";
import { Button } from "../ui/button";
import { useCallback, useEffect, useMemo, useState } from "react";
import { debounce } from "lodash";
import { AxiosError } from "axios";
import { ApiError, CartItem } from "@/lib/types";
import { toast } from "@/hooks/use-toast";
import { deleteCartItem } from "@/lib/api/cart/deleteCartItem";
import { CART_KEY } from "@/hooks/cart/useCartList";
import { useSWRConfig } from "swr";
import { updateCartQuantity } from "@/lib/api/cart/updateQuantity";

const CartQuantityButton = ({
  itemId,
  product,
}: {
  itemId: number;
  product: CartItem;
}) => {
  const { mutate } = useSWRConfig();
  const [newQuantity, setNewQuantity] = useState(product.quantity);

  const handleDeleteCartItem = useCallback(() => {
    deleteCartItem(itemId)
      .then(() => {
        mutate(CART_KEY);
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Delete cart item failed",
        });
      });
  }, [itemId, mutate]);

  const changeQuantity = useMemo(
    () =>
      debounce(async (quantity: number) => {
        if (quantity > 0) {
          updateCartQuantity(itemId, {
            quantityChange: quantity - product.quantity,
          })
            .then(() => {
              mutate(CART_KEY);
            })
            .catch((err: AxiosError<ApiError>) => {
              toast({
                variant: "destructive",
                title: "Error",
                description:
                  err.response?.data.message ?? "Update quantity failed",
              });
            });
        } else {
          handleDeleteCartItem();
        }
      }, 500),
    [itemId, product.quantity, mutate, handleDeleteCartItem]
  );

  const handleButtonClick = (value: number) => {
    setNewQuantity((prev) => {
      const newQuantity = prev + value;
      if (newQuantity < 0) {
        return 0;
      } else {
        changeQuantity(newQuantity);
      }
      return newQuantity;
    });
  };

  useEffect(() => {
    setNewQuantity(product.quantity);
  }, [product.quantity]);

  return (
    <div className="rounded-full border flex items-center">
      <Button
        type="button"
        variant={"ghost"}
        size={"icon"}
        className="rounded-full"
        onClick={() => handleButtonClick(-1)}
      >
        {newQuantity < 2 ? (
          <LuTrash2 className="!h-5 !w-5" />
        ) : (
          <LuMinus className="!h-5 !w-5" />
        )}
      </Button>
      <div className="font-medium text-base w-6 text-center">{newQuantity}</div>
      <Button
        type="button"
        variant={"ghost"}
        size={"icon"}
        className="rounded-full"
        disabled={newQuantity === product.itemQuantity}
        onClick={() => handleButtonClick(1)}
      >
        <LuPlus className="!h-5 !w-5" />
      </Button>
    </div>
  );
};

export default CartQuantityButton;
