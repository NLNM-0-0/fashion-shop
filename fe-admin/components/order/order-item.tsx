import { Order } from "@/lib/types";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import OrderDetailItem from "./order-detail-item";
import OrderStatusView from "./order-status-view";
import { OrderStatus } from "@/lib/constants/enum";

interface OrderItemProps {
  order: Order;
}
const OrderItem = ({ order }: OrderItemProps) => {
  return (
    <div
      className={`p-4 flex flex-col gap-2 rounded-xl shadow-[0px_1px_2px_0px_rgba(0,0,0,0.1)] hover:shadow-md border overflow-clip`}
    >
      <div className="flex justify-between items-center border-b pb-3">
        {order.customer ? (
          <div className="flex gap-2 items-center">
            <Avatar>
              <AvatarImage src={order.customer.image ?? ""} alt="avatar" />
              <AvatarFallback>
                {order.customer.name.substring(0, 2)}
              </AvatarFallback>
            </Avatar>
            <div className="text-base">
              <span className="font-medium">{order.customer.name}</span>
            </div>
          </div>
        ) : (
          <div></div>
        )}
        <OrderStatusView status={order.orderStatus as OrderStatus} />
      </div>
      {order.details.map((detail, index) => (
        <OrderDetailItem key={`order-detail-${index}`} detail={detail} />
      ))}
      <span className="text-lg font-medium self-end">
        Total ({order.totalQuantity} item{order.totalQuantity > 1 ? "s" : ""}):{" "}
        {new Intl.NumberFormat("vi-VN", {
          style: "currency",
          currency: "VND",
        }).format(order.totalPrice)}
      </span>
    </div>
  );
};

export default OrderItem;