import CartList from "@/components/cart/cart-list";
import { Metadata } from "next";
export const metadata: Metadata = {
  title: "Cart",
};
const CartPage = () => {
  return (
    <>
      <CartList />
    </>
  );
};

export default CartPage;
