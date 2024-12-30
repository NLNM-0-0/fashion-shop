import SWRProvider from "@/components/auth/swr-provider";
import CartList from "@/components/cart/cart-list";
import CartListSkeleton from "@/components/cart/cart-list-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";
export const metadata: Metadata = {
  title: "Cart",
};
const CartPage = () => {
  return (
    <>
      <Suspense fallback={<CartListSkeleton />}>
        <SWRProvider>
          <CartList />
        </SWRProvider>
      </Suspense>
    </>
  );
};

export default CartPage;
