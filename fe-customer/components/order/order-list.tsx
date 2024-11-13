"use client";
import { orderFilterValues, orderStatusValues } from "@/lib/constants";
import { FormFilterItem, FormFilterValues } from "@/lib/types";
import StatusButton from "../ui/status-button";
import { useState } from "react";
import Filter from "../filter/filter";
import { FilterParams } from "@/hooks/useFilterList";
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
    updateFilter,
    updateFilters,
  } = useOrderList();

  const [openFilter, setOpenFilter] = useState(false);

  const onApplyFilters = (data: FormFilterValues) => {
    const filterParams = data.filters.reduce(
      (acc: FilterParams, item: FormFilterItem) => {
        acc[item.type] = item.value;
        return acc;
      },
      {}
    );
    updateFilters(filterParams);
  };
  if (error) return <div>Error loading orders.</div>;
  if (isLoadingMore) return <OrderListSkeleton number={5} />;
  return (
    <div className="flex flex-1  justify-center grow overflow-y-auto">
      <div className="flex flex-1 flex-col gap-2 max-w-[1200px]">
        <div className="flex justify-between items-center flex-wrap">
          <Filter
            title="Filter orders"
            filters={filters}
            filterValues={orderFilterValues}
            open={openFilter}
            onOpenChange={(open) => {
              setOpenFilter(open);
            }}
            onApplyFilters={onApplyFilters}
          />
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
                onSelect={(value) => updateFilter("orderStatus", value)}
              />
            ))}
          </div>
        </div>
        <div className="flex flex-col mt-4 gap-4">
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
