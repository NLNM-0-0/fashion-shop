import { IconType } from "react-icons";
import { FilterInputType } from "../constants/enum";

export interface SidebarItem {
  title: string;
  href: string;
  icon?: IconType;
  submenu?: boolean;
  subMenuItems?: SidebarItem[];
}

export interface User {
  email: string;
  name: string;
  phone: string;
  dob: string;
  address: string;
  image: string;
  male: boolean;
}

export interface SignupUser extends User {
  password: string;
}

export interface Customer extends User {
  id: number;
}

export interface Staff extends Customer {
  admin: boolean;
}

export interface CustomerData {
  data: Customer[];
  page: PagingType;
}

export interface StaffData {
  data: Staff[];
  page: PagingType;
}
export interface Feature {
  id: number;
  code: string;
  name: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface LoginToken {
  token: string;
}

export interface PagingParam {
  page?: number;
  limit?: number;
}

export interface PagingType {
  index: number;
  limit: number;
  totalPages: 2;
}

export interface CustomerFilterParam extends PagingParam {
  name?: string;
  email?: string;
  phone?: string;
}

export interface StaffFilterParam extends PagingParam {
  name?: string;
  email?: string;
  admin?: boolean;
  male?: boolean;
  monthDOB?: number;
  yearDOB?: number;
}
export interface FormFilterItem {
  type: string;
  value: string;
}

export interface FormFilterType {
  type: string;
  title: string;
  inputType: FilterInputType;
}

export interface FormFilterValues {
  filters: FormFilterItem[];
}
