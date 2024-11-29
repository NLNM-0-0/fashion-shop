import { Order } from "@/lib/types";
import OrderDetailItem from "./order-detail-item";
import OrderStatusView from "./order-status-view";
import Link from "next/link";
import { toVND } from "@/lib/utils";

interface OrderItemProps {
  order: Order;
}
const OrderItem = ({ order }: OrderItemProps) => {
  return (
    <Link
      href={`/fa/order/${order.id}`}
      className={`p-4 flex flex-col gap-2 rounded-xl shadow-[0px_1px_2px_0px_rgba(0,0,0,0.1)] hover:shadow-md border overflow-clip cursor-pointer`}
    >
      <div className="flex justify-between items-center border-b pb-3">
        <div className="font-medium">Order date: {order.createdAt}</div>
        <OrderStatusView status={order.orderStatus} />
      </div>
      {order.details.map((detail, index) => (
        <OrderDetailItem key={`order-detail-${index}`} detail={detail} />
      ))}
      <span className="text-lg font-medium self-end">
        Total ({order.totalQuantity} item{order.totalQuantity > 1 ? "s" : ""}):{" "}
        {toVND(order.totalPrice)}
      </span>
    </Link>
  );
};

export default OrderItem;
