import { BaseProduct } from "@/lib/types";
import { AspectRatio } from "../ui/aspect-ratio";
import Image from "next/image";
import { toVND } from "@/lib/utils";
import Link from "next/link";

const ProductItem = ({ product }: { product: BaseProduct }) => {
  return (
    <div className="flex flex-col bg-white font-medium pb-10">
      <AspectRatio ratio={1 / 1} className="bg-muted">
        <Link href={`/fa/products/${product.id}`}>
          <Image
            src={product.images.at(0) ?? ""}
            alt="prd"
            fill
            className="h-full w-full rounded-md object-cover"
          />
        </Link>
      </AspectRatio>
      <div className="flex justify-between mt-3 items-start">
        <div>
          <Link href={`/fa/products/${product.id}`}>{product.name}</Link>
          <div>{toVND(product.unitPrice)}</div>
        </div>
      </div>
    </div>
  );
};

export default ProductItem;
