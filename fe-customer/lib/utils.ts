import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";
import { Product } from "./types";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function toVND(value: number) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(value);
}

export function getSizesFromColor(product: Product, colorName?: string) {
  if (!colorName) {
    return [];
  }
  return Object.entries(product.quantities)
    .map(([key, value]) => {
      const [sizeName, colorName] = key.split("-");
      return {
        quantityKey: key,
        size: sizeName,
        color: colorName,
        quantity: value,
      };
    })
    .filter(
      (quantity) => quantity.color === colorName && quantity.quantity > 0
    );
}
