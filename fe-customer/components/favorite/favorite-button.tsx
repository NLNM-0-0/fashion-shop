"use client";
import Link from "next/link";
import { buttonVariants } from "../ui/button";
import { cn } from "@/lib/utils";
import Heart from "@/lib/assets/icons/heart.svg";
import Image from "next/image";

const FavoriteButton = () => {
  return (
    <Link
      href={"/fa/favorite"}
      className={cn(
        buttonVariants({ variant: "ghost" }),
        "rounded-full w-9 h-9 p-0"
      )}
    >
      <Image src={Heart.src} alt="cart" height={20} width={20} />
    </Link>
  );
};

export default FavoriteButton;
