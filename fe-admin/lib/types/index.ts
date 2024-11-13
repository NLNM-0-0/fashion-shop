import { IconType } from "react-icons";
import { FilterInputType, OrderStatus } from "../constants/enum";

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

export interface OrderStaff {
  id: number;
  email: string;
  name: string;
  image: string;
}

export interface OrderCustomer {
  id: number;
  email: string;
  phone: number;
  name: string;
  image: string;
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

export interface QuantityPayload {
  size: string;
  color: string;
  quantity: number;
}
export interface CreateProductPayload {
  name: string;
  gender: string;
  season: string;
  images: string[];
  categories: number[];
  quantities: QuantityPayload[];
  unitPrice: number;
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

export interface ProductData {
  data: Product[];
  page: PagingType;
}

export interface OrderData {
  data: Order[];
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
  expired: string;
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

export interface ProductFilterParam extends PagingParam {
  name?: string;
}

export interface OrderFilterParam extends PagingParam {
  orderStatus?: OrderStatus;
  staffName?: string;
  customerName?: string;
}

export interface StaffFilterParam extends PagingParam {
  name?: string;
  email?: string;
  admin?: boolean;
  male?: boolean;
  monthDOB?: number;
  yearDOB?: number;
}

// Memo: no paging param
export interface ReportFilterParam extends PagingParam {
  timeFrom?: number;
  timeTo?: number;
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

export interface OrderStatusValue {
  title: string;
  value: OrderStatus;
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
export interface Size {
  name: string;
}
export interface Color {
  name: string;
  hex: string;
}
export interface Product {
  id: number;
  name: string;
  gender: string;
  season: string;
  images: string[];
  sizes: Size[];
  colors: Color[];
  categories: Category[];
  quantities: Record<string, number>;
  unitPrice: number;
  delete: boolean;
}

export interface Order {
  id: number;
  customer: OrderCustomer;
  totalPrice: number;
  totalQuantity: number;
  staff: OrderStaff;
  createdAt: string;
  updatedAt: string;
  orderStatus: OrderStatus;
  details: OrderDetail[];
  confirmedAt: string | null;
  shippingAt: string | null;
  doneAt: string | null;
  canceledAt: string | null;
}

export interface Category {
  id: number;
  name: string;
}

export interface CategoryData {
  data: Category[];
}

export interface OrderDetail {
  item: Product;
  quantity: number;
  unitPrice: number;
  totalSubPrice: number;
}
export interface SaleReportItem {
  item: Product;
  amount: number;
  totalSales: number;
}

export interface StockReportItem {
  item: Product;
  initial: number;
  sell: number;
  increase: number;
  decrease: number;
  payback: number;
  final: number;
}

export interface SaleReportData {
  total: number;
  amount: number;
  details: SaleReportItem[];
}

export interface StockReportData {
  initial: number;
  sell: number;
  increase: number;
  decrease: number;
  payback: number;
  final: number;
  details: StockReportItem[];
}
