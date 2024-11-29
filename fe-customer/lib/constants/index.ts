import { FormFilterType, OrderStatusValue } from "../types";
import { Color, FilterInputType, OrderStatus, Price } from "./enum";
import { LucideIcon } from "lucide-react";
import { Menu, PackageCheck, PackageX, Truck, Receipt } from "lucide-react";

export const notiFilterValues: FormFilterType[] = [
  { type: "sender", title: "Sender Name", inputType: FilterInputType.TEXT },
  { type: "fromDate", title: "From Date", inputType: FilterInputType.DATE },
  { type: "toDate", title: "To Date", inputType: FilterInputType.DATE },
  {
    type: "seen",
    title: "Seen",
    inputType: FilterInputType.BOOLEAN,
    trueTitle: "Seen",
    falseTitle: "Not Seen",
  },
];

export const productFilterValues: FormFilterType[] = [
  { type: "name", title: "Name", inputType: FilterInputType.TEXT },
];

export const orderFilterValues: FormFilterType[] = [
  {
    type: "staffName",
    title: "Staff Name",
    inputType: FilterInputType.TEXT,
  },
  {
    type: "customerName",
    title: "Customer Name",
    inputType: FilterInputType.TEXT,
  },
];

export const orderStatusValues: OrderStatusValue[] = [
  { title: "Pending", value: OrderStatus.PENDING },
  { title: "Confirmed", value: OrderStatus.CONFIRMED },
  { title: "Delivering", value: OrderStatus.SHIPPING },
  { title: "Complete", value: OrderStatus.DONE },
  { title: "Cancelled", value: OrderStatus.CANCELED },
];

export const orderStatusColors: Record<OrderStatus, string> = {
  [OrderStatus.PENDING]: "#fb923c",
  [OrderStatus.CONFIRMED]: "#0891b2",
  [OrderStatus.SHIPPING]: "#15803d",
  [OrderStatus.DONE]: "#0284c7",
  [OrderStatus.CANCELED]: "#b91c1c",
};

export const orderStatusTitle: Record<OrderStatus, string> = {
  [OrderStatus.PENDING]: "Pending",
  [OrderStatus.CONFIRMED]: "Confirmed",
  [OrderStatus.SHIPPING]: "Delivering",
  [OrderStatus.DONE]: "Complete",
  [OrderStatus.CANCELED]: "Cancelled",
};

export const orderStatusIcons: Record<OrderStatus, LucideIcon> = {
  [OrderStatus.PENDING]: Menu,
  [OrderStatus.CONFIRMED]: Receipt,
  [OrderStatus.SHIPPING]: Truck,
  [OrderStatus.DONE]: PackageCheck,
  [OrderStatus.CANCELED]: PackageX,
};

export const colorToClassMap: Record<Color, string> = {
  [Color.BLACK]: "#000",
  [Color.BLUE]: "#0000FF",
  [Color.BROWN]: "#8B4513",
  [Color.GREEN]: "#008000",
  [Color.GREY]: "#808080",
  [Color.ORANGE]: "#FFA500",
  [Color.PINK]: "#FFC0CB",
  [Color.PURPLE]: "#800080",
  [Color.RED]: "#FF0000",
  [Color.WHITE]: "#FFFFFF",
  [Color.YELLOW]: "#facc15",
  [Color.MULTI_COLOR]: "",
};

export const priceToTitle: Record<Price, string> = {
  [Price.ALL]: "All",
  [Price.BELOW199]: "< 199K",
  [Price.FROM199TO299]: "199K - 299K",
  [Price.FROM299TO399]: "299K - 399K",
  [Price.FROM399TO499]: "399K - 499K",
  [Price.FROM499TO799]: "499K - 799K",
  [Price.FROM799TO999]: "799K - 999K",
  [Price.ABOVE999]: "> 999K",
};
