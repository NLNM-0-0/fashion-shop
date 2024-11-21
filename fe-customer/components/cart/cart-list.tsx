"use client";
import { useCartList } from "@/hooks/cart/useCartList";
import CartListItem from "./cart-item";
import { toVND } from "@/lib/utils";
import { Button } from "../ui/button";
import { createOrder } from "@/lib/api/order/createOrder";
import { toast } from "@/hooks/use-toast";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import CartListSkeleton from "./cart-list-skeleton";

const CartList = () => {
  const { data, isLoading, error } = useCartList();
  const price = data?.data.data.reduce(
    (total, item) => total + item.item.unitPrice,
    0
  );

  const handleCreateOrder = () => {
    if (!data || data?.data.data.length < 1) {
      toast({
        variant: "destructive",
        title: "Error",
        description: "Please have at least one item in bag",
      });
      return;
    }
    createOrder({
      details: data?.data.data.map((item) => {
        return {
          itemId: item.item.id,
          size: item.size,
          color: item.color,
          quantity: item.quantity,
        };
      }),
    })
      .then(() => {
        toast({
          variant: "success",
          title: "Success",
          description: "Create order successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Update quantity failed",
        });
      });
  };

  if (error) return <>Failed to load</>;
  else if (isLoading || !data)
    return (
      <>
        <CartListSkeleton />
      </>
    );
  return (
    <div className="flex gap-10 lg:flex-row flex-col">
      <div className="flex basis-2/3 flex-col">
        <div className="table___title mb-4">Bag</div>
        {data.data.data.length > 0 ? (
          data.data.data.map((item) => (
            <CartListItem product={item} key={item.id} itemId={item.id} />
          ))
        ) : (
          <>There are no items in your bag.</>
        )}
      </div>

      <div className="flex basis-1/3 w-full flex-col self-start min-w-96">
        <span className="table___title mb-10">Summary</span>
        <div className="flex justify-between">
          <span>Subtotal</span>
          <span>{toVND(price ?? 0)}</span>
        </div>
        <div className="flex justify-between mt-5">
          <span>Estimated Delivery & Handling</span>
          <span>Free</span>
        </div>
        <div className="flex justify-between mt-5 py-4 border-y">
          <span>Total</span>
          <span>
            <span>{toVND(price ?? 0)}</span>
          </span>
        </div>
        <Button className="h-12 rounded-full mt-5" onClick={handleCreateOrder}>
          Checkout
        </Button>
      </div>
    </div>
  );
};

export default CartList;
