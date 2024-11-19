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
  title: string;
}

export function statusToTimeMap(order: Order): StatusTimeLine[] {
  return [
    {
      label: OrderStatus.CONFIRMED,
      time: order.confirmedAt,
      title: "Confirmed at",
    },
    {
      label: OrderStatus.SHIPPING,
      time: order.shippingAt,
      title: "Start shipping at",
    },
    {
      label: OrderStatus.DONE,
      time: order.doneAt,
      title: "Order completed at",
    },
    {
      label: OrderStatus.CANCELED,
      time: order.canceledAt,
      title: "Cancelled order at",
    },
  ];
}

export const getStatusTimeline = (order: Order): StatusTimeLine[] => {
  const statusTimeline = statusToTimeMap(order);
  return statusTimeline.filter(({ time }) => time !== null);
};
