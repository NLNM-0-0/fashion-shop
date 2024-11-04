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
export interface UserInfo extends User {
  id: number;
  admin: boolean;
}
export interface SignupUser extends User {
  password: string;
}

export interface Customer extends User {
  id: number;
}

export interface Staff {
  id: number;
  email: string;
  name: string;
  dob: string;
  address: string;
  image: string;
  male: boolean;
  admin: boolean;
}

export interface CreateStaffPayload {
  email: string;
  name: string;
  dob: string;
  address: string;
  image: string;
  male: boolean;
  admin: boolean;
}

export interface CreateNotificationPayload {
  title: string;
  description: string;
  receiver: number[];
}

export interface CustomerData {
  data: Customer[];
  page: PagingType;
}

export interface StaffData {
  data: Staff[];
  page: PagingType;
}

export interface NotificationData {
  data: Notification[];
  page: PagingType;
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
  totalElements: number;
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

export interface NotiFilterParam extends PagingParam {
  sender?: string;
  fromDate?: string;
  toDate?: string;
  seen?: boolean;
}
export interface FormFilterItem {
  type: string;
  value: string;
}

export interface FormFilterType {
  type: string;
  title: string;
  inputType: FilterInputType;
  trueTitle?: string;
  falseTitle?: string;
}

export interface FormFilterValues {
  filters: FormFilterItem[];
}

export interface ChangePasswordPayload {
  oldPassword: string;
  newPassword: string;
}

export interface ApiError {
  timestamp: string;
  status: string;
  message: string;
  detail: string;
}

export interface Notification {
  id: number;
  title: string;
  description: string;
  from: {
    id: number;
    email: string;
    phone: string;
    name: string;
    image: string;
  };
  to: {
    id: number;
    email: string;
    phone: string;
    name: string;
    image: string;
  };
  createdAt: number;
  seen: boolean;
}
