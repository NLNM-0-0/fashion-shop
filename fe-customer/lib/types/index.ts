import { FilterInputType, OrderStatus } from "../constants/enum";

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
}
export interface SignupPayload extends User {
  password: string;
}

export interface SuccessResponse {
  data: boolean;
}

export interface Customer extends User {
  id: number;
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

export interface CreateProductPayload {
  name: string;
  image: string;
  unitPrice: number;
  quantity: number;
}

export interface CreateNotificationPayload {
  title: string;
  description: string;
  receiver: number[];
}

export interface ProductData {
  data: Product[];
  page: PagingType;
}

export interface ProductList {
  data: Product[];
}

export interface Category {
  id: number;
  name: string;
}

export interface CategoryData {
  data: Category[];
}

export interface OrderData {
  data: Order[];
  page: PagingType;
}

export interface NotificationData {
  data: Notification[];
  page: PagingType;
}

export interface LoginPayload {
  phone: string;
  password: string;
}

export interface LoginToken {
  token: string;
  expired: string;
}

export interface SendOtpPayload {
  phone: string;
}

export interface VerifyOtpPayload {
  phone: string;
  otp: string;
}

export interface ForgotPasswordPayload extends VerifyOtpPayload {
  password: string;
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

export interface ProductFilterParam extends PagingParam {
  name?: string;
}

export interface OrderFilterParam extends PagingParam {
  orderStatus?: OrderStatus;
  staffName?: string;
  customerName?: string;
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
  value: string | string[];
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

export interface ProductColor {
  name: string;
  hex: string;
}
export interface Size {
  name: string;
}

export interface Product {
  id: number;
  name: string;
  gender: string;
  season: string;
  images: string[];
  sizes: Size[];
  colors: ProductColor[];
  categories: Category[];
  quantities: Record<string, number>;
  unitPrice: number;
  delete: boolean;
  liked: boolean;
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
  name: string;
  phone: string;
  address: string;
}

export interface OrderDetail {
  item: Product;
  quantity: number;
  unitPrice: number;
  totalSubPrice: number;
  color: string;
  size: string;
}

export interface AddToCartPayload {
  itemId: number;
  size: string;
  color: string;
  quantity: number;
}

export interface CartItemDetail {
  id: number;
  name: string;
  images: string[];
  unitPrice: number;
  delete: boolean;
}

export interface CartItem {
  id: number;
  item: Product;
  size: string;
  color: string;
  quantity: number;
  itemQuantity: number;
  exist: true;
}

export interface CartListData {
  data: CartItem[];
}

export interface BaseProduct {
  id: number;
  name: string;
  images: string[];
  unitPrice: number;
}

export interface FavoriteData {
  data: BaseProduct[];
}
