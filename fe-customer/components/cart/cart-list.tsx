"use client";
import { useCartList } from "@/hooks/cart/useCartList";
import CartListItem from "./cart-item";
import { toVND } from "@/lib/utils";
import { Button } from "../ui/button";

const CartList = () => {
  const { data, isLoading, error } = useCartList();
  const price = data?.data.data.reduce(
    (total, item) => total + item.item.unitPrice,
    0
  );
  if (error) return <>Failed to load</>;
  else if (isLoading || !data) return <>Skeleton...</>;
  return (
    <div className="flex gap-10">
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

      <div className="flex basis-1/3 flex-col self-start">
        <span className="table___title font-medium mb-10">Summary</span>
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
        <Button className="h-12 rounded-full mt-5">Check out</Button>
      </div>
    </div>
  );
};

export default CartList;
