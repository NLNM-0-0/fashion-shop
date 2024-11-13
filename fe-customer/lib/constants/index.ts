import { Category, FormFilterType, OrderStatusValue, Product } from "../types";
import { FilterInputType, Gender, OrderStatus } from "./enum";
import { LucideIcon } from "lucide-react";
import { Menu, PackageCheck, PackageX, Truck, Receipt } from "lucide-react";

export const customerFilterValues: FormFilterType[] = [
  { type: "name", title: "Name", inputType: FilterInputType.TEXT },
  { type: "email", title: "Email", inputType: FilterInputType.TEXT },
  { type: "phone", title: "Phone", inputType: FilterInputType.TEXT },
];

export const staffFilterValues: FormFilterType[] = [
  { type: "name", title: "Name", inputType: FilterInputType.TEXT },
  { type: "email", title: "Email", inputType: FilterInputType.TEXT },
  {
    type: "male",
    title: "Gender",
    inputType: FilterInputType.BOOLEAN,
    trueTitle: "Male",
    falseTitle: "Female",
  },
  {
    type: "monthDOB",
    title: "Month of birth",
    inputType: FilterInputType.MONTH,
  },
  {
    type: "yearDOB",
    title: "Year of birth",
    inputType: FilterInputType.YEAR,
  },
];

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
  [OrderStatus.PENDING]: "bg-orange-400",
  [OrderStatus.CONFIRMED]: "bg-cyan-600",
  [OrderStatus.SHIPPING]: "bg-green-700",
  [OrderStatus.DONE]: "bg-sky-600",
  [OrderStatus.CANCELED]: "bg-red-700",
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

const categories: Category[] = [
  {
    id: 1,
    name: "Shorts",
  },
];
export const sampleProduct: Product = {
  id: 1,
  name: "This is a sample product",
  gender: Gender.UNISEX,
  colors: [
    { name: "BLUE", hex: "#0000FF" },
    { name: "BLACK", hex: "#000" },
    { name: "GREY", hex: "#808080" },
  ],
  categories: categories,
  sizes: [{ name: "S" }, { name: "M" }],
  season: "SPRING",
  images: ["/images/1.png", "/images/2.png", "/images/3.png"],
  price: 3675000,
  deleted: false,
};
