import { OrderDetail } from "@/lib/types";
import { toVND } from "@/lib/utils";
import Image from "next/image";
const OrderDetailItem = ({ detail }: { detail: OrderDetail }) => {
  return (
    <div className="w-full flex flex-1 gap-3 pb-3 border-b">
      <Image
        alt="prd"
        src={detail.item.image}
        height={100}
        width={100}
        className="h-24 w-24 object-contain"
      />
      <div className="flex flex-col flex-1 gap-1">
        <span className="truncate w-full text-base font-medium">
          {detail.item.name}
        </span>
        <div className="flex justify-between text-fs-gray-dark text-sm">
          <span>Size: S</span>
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
