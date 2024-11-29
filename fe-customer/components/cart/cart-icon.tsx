"use client";
import Link from "next/link";
import { buttonVariants } from "../ui/button";
import { cn } from "@/lib/utils";
import Cart from "@/lib/assets/icons/bag.svg";
import Image from "next/image";
import { useCartNumber } from "@/hooks/cart/useCartNumber";
const CartButton = () => {
  const { data } = useCartNumber();

  return (
    <Link
      href={"/fa/cart"}
      className={cn(
        buttonVariants({ variant: "ghost" }),
        "rounded-full w-9 h-9 p-0 relative"
      )}
    >
      <Image src={Cart.src} alt="cart" height={20} width={20} />
      {data && data.data.number > 0 && (
        <span className="absolute top-2.5 text-[10px]">
          {data?.data.number ?? 0}
        </span>
      )}
    </Link>
  );
};

export default CartButton;
