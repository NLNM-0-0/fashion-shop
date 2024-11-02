import { FormFilterType, SidebarItem } from "../types";
import { GoPeople, GoPerson } from "react-icons/go";
import { FilterInputType } from "./enum";

export const sidebarItems: SidebarItem[] = [
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
  { type: "male", title: "Gender", inputType: FilterInputType.GENDER },
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
