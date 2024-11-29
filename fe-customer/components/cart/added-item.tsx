import { toVND } from "@/lib/utils";
import Image from "next/image";
import { AddedItemProps } from "./added-to-bag-dialog";
const AddedItem = ({ product }: { product: AddedItemProps }) => {
  return (
    <div className="flex gap-2 pb-4">
      <Image
        alt="prd"
        width={100}
        height={100}
        src={product.item.images.at(0) ?? ""}
      />
      <div>
        <p className="font-medium">{product.item.name}</p>
        <p className="text-fs-gray-darker capitalize">
          Color: {product.color.toLowerCase()}
        </p>
        <p className="text-fs-gray-darker">Size: {product.size}</p>
        <p className="font-medium">{toVND(product.item.unitPrice)}</p>
      </div>
    </div>
  );
};

export default AddedItem;
