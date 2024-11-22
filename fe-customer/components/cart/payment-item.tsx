import { CartItem } from "@/lib/types";
import Image from "next/image";
import { toVND } from "@/lib/utils";

const PaymentItem = ({ product }: { product: CartItem }) => {
  return (
    <div className="flex gap-4 py-4">
      <Image
        src={product.item.images.at(0) ?? ""}
        alt="prd"
        width={164}
        height={164}
        className="object-cover h-[164px] w-[164px]"
      />
      <div className="flex-1 flex flex-col gap-1 text-base">
        <h1 className="tracking-wide font-medium whitespace-nowrap">
          {product.item.name}
        </h1>
        <span className="text-base text-fs-gray-darker">
          Qty: {product.quantity}
        </span>
        <span className="text-base text-fs-gray-darker">
          Color: {product.color}
        </span>
        <span className="text-base text-fs-gray-darker">
          Size: {product.size}
        </span>
        <span className="tracking-wide font-medium mt-2">
          {toVND(product.item.unitPrice)}
        </span>
      </div>
    </div>
  );
};

export default PaymentItem;
