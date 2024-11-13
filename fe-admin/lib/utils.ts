import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";
import { Order } from "./types";
import { OrderStatus } from "./constants/enum";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function toVND(value: number) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(value);
}

export interface StatusTimeLine {
  label: OrderStatus;
  time: string | null;
}

export function statusToTimeMap(order: Order): StatusTimeLine[] {
  return [
    {
      label: OrderStatus.CONFIRMED,
      time: order.confirmedAt,
    },
    {
      label: OrderStatus.SHIPPING,
      time: order.shippingAt,
    },
    {
      label: OrderStatus.DONE,
      time: order.doneAt,
    },
    {
      label: OrderStatus.CANCELED,
      time: order.canceledAt,
    },
  ];
}

export const getStatusTimeline = (order: Order): StatusTimeLine[] => {
  const statusTimeline = statusToTimeMap(order);

  return statusTimeline.filter(({ time }) => time !== null);
};

