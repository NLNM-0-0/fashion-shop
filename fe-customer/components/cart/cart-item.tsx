import { CartItem } from "@/lib/types";
import Image from "next/image";
import { Button } from "../ui/button";
import { toVND } from "@/lib/utils";
import CartQuantityButton from "./cart-quantity-button";
import Heart from "@/lib/assets/icons/heart.svg";
import UpdateSizeDialog from "./update-size-dialog";
import UpdateColorDialog from "./update-color-dialog";
import Link from "next/link";

const CartListItem = ({
  product,
  itemId,
}: {
  product: CartItem;
  itemId: number;
}) => {
  return (
    <div className="flex gap-4 pt-6 py-10 border-b lg:last:border-b-0">
      <div className="flex flex-col gap-3">
        <Link href={`/fa/products/${product.item.id}`}>
          <Image
            src={product.item.images.at(0) ?? ""}
            alt="prd"
            width={164}
            height={164}
            className="object-cover h-[164px] w-[164px]"
          />
        </Link>
        <div className="flex justify-between">
          <CartQuantityButton product={product} itemId={itemId} />
          <Button
            variant={"ghost"}
            type="button"
            className="rounded-full"
            size={"icon"}
          >
            <Image src={Heart.src} alt="cart" height={20} width={20} />
          </Button>
        </div>
      </div>
      <div className="flex-1 flex justify-between sm:flex-row flex-col-reverse">
        <div className="flex-1 flex flex-col gap-1 text-base">
          <Link href={`/fa/products/${product.item.id}`}>
            <h1 className="tracking-wide font-medium whitespace-nowrap">
              {product.item.name}
            </h1>
          </Link>
          <span className="text-base text-fs-gray-darker">
            Color:
            <UpdateColorDialog cartItem={product} />
          </span>
          <span className="text-base text-fs-gray-darker">
            Size:
            <UpdateSizeDialog cartItem={product} />
          </span>
        </div>
        <span className="tracking-wide font-medium">
          {toVND(product.item.unitPrice)}
        </span>
      </div>
    </div>
  );
};

export default CartListItem;
