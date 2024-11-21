import { OrderDetail } from "@/lib/types";
import { toVND } from "@/lib/utils";
import Image from "next/image";
const OrderDetailItem = ({ detail }: { detail: OrderDetail }) => {
  return (
    <div className="w-full flex flex-1 gap-3 pb-3 border-b last:border-none">
      <Image
        src={detail.item.images.at(0) ?? ""}
        alt="prd"
        width={164}
        height={164}
        className="object-cover h-[164px] w-[164px]"
      />
      <div className="flex flex-col flex-1 gap-1">
        <span className="truncate w-full text-base font-medium">
          {detail.item.name}
        </span>
        <div className="flex justify-between text-fs-gray-dark text-sm">
          <div className="flex flex-col">
            <span className="text-fs-gray-darker">Color: {detail.color}</span>
            <span className="text-fs-gray-darker">Size: {detail.size}</span>
          </div>
          <span>x {detail.quantity}</span>
        </div>
        <div className="self-end">
          <span>{toVND(detail.unitPrice)}</span>
        </div>
      </div>
    </div>
  );
};

export default OrderDetailItem;
