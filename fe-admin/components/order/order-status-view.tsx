import { orderStatusColors, orderStatusTitle } from "@/lib/constants";
import { OrderStatus } from "@/lib/constants/enum";
import { cn } from "@/lib/utils";

const OrderStatusView = ({ status }: { status: OrderStatus }) => {
  return (
    <div
      className={cn(
        orderStatusColors[status],
        "rounded-xl px-3 py-1.5 text-sm font-medium text-white tracking-wide"
      )}
    >
      {orderStatusTitle[status]}
    </div>
  );
};

export default OrderStatusView;
