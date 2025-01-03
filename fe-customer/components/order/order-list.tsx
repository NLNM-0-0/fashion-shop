"use client";
import { orderStatusValues } from "@/lib/constants";
import StatusButton from "../ui/status-button";
import { useOrderList } from "@/hooks/useOrderList";
import OrderItem from "./order-item";
import OrderListSkeleton from "./order-list-skeleton";

const OrderList = () => {
  const {
    orders,
    isLoadingMore,
    error,
    lastItemRef,
    filters,
    removeFilter,
    setSize,
    updateFilter,
  } = useOrderList();

  if (error) return <div>Error loading orders.</div>;
  if (isLoadingMore) return <OrderListSkeleton number={5} />;
  return (
    <div className="flex flex-1  justify-center grow overflow-y-auto">
      <div className="flex flex-1 flex-col gap-2 max-w-[1200px]">
        <div className="flex justify-end  items-center flex-wrap">
          <div className="flex gap-2 flex-wrap">
            <StatusButton
              title="All"
              value=""
              selected={!filters.orderStatus}
              onSelect={() => removeFilter("orderStatus")}
            />
            {orderStatusValues.map((status) => (
              <StatusButton
                key={status.value}
                title={status.title}
                value={status.value}
                selected={filters.orderStatus === status.value}
                onSelect={(value) => {
                  updateFilter("orderStatus", value);
                  setSize(1);
                }}
              />
            ))}
          </div>
        </div>
        <div className="flex flex-col mt-4 gap-4">
          {orders.length < 1 && <>There are no orders found.</>}
          {orders.map((item, index) => (
            <div
              key={item.id}
              ref={index === orders.length - 1 ? lastItemRef : null}
            >
              <OrderItem order={item} />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default OrderList;
