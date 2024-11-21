import { FormFilterType, OrderStatusValue, SidebarItem } from "../types";
import { GoPeople, GoPerson } from "react-icons/go";
import { Color, FilterInputType, OrderStatus } from "./enum";
import { PiDress } from "react-icons/pi";
import { LuClipboardList } from "react-icons/lu";
import { AiOutlineLineChart } from "react-icons/ai";
import { BiCategory } from "react-icons/bi";
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
    title: "Category",
    href: "/admin/category",
    icon: BiCategory,
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
export const orderStatusToNextTitle: Record<OrderStatus, string | undefined> = {
  [OrderStatus.PENDING]: "Confirm Order",
  [OrderStatus.CONFIRMED]: "Ship Order",
  [OrderStatus.SHIPPING]: "Complete Order",
  [OrderStatus.DONE]: undefined,
  [OrderStatus.CANCELED]: undefined,
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
};
