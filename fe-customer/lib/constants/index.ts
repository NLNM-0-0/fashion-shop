import { FormFilterType, OrderStatusValue, SidebarItem } from "../types";
import { GoPeople, GoPerson } from "react-icons/go";
import { FilterInputType, OrderStatus } from "./enum";
import { PiDress } from "react-icons/pi";
import { LuClipboardList } from "react-icons/lu";
import { AiOutlineLineChart } from "react-icons/ai";
import { LucideIcon } from "lucide-react";
import { Menu, PackageCheck, PackageX, Truck, Receipt } from "lucide-react";
export const sidebarItems: SidebarItem[] = [
  {
    title: "Products",
    href: "/admin/products",
    icon: PiDress,
    submenu: false,
  },
  {
    title: "Orders",
    href: "/admin/order",
    icon: LuClipboardList,
    submenu: false,
  },
  {
    title: "Report",
    href: "/admin/report",
    icon: AiOutlineLineChart,
    submenu: true,
    subMenuItems: [
      { title: "Sale Report", href: "/admin/report/sale" },
      { title: "Stock Report", href: "/admin/report/stock" },
    ],
  },
  {
    title: "Customers",
    href: "/admin/customer",
    icon: GoPerson,
    submenu: false,
  },
  {
    title: "Staffs",
    href: "/admin/staff",
    icon: GoPeople,
    submenu: false,
  },
];

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
