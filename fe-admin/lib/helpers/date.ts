import { format } from "date-fns";
import { vi } from "date-fns/locale";

export const stringToDate = (value: string) => {
  try {
    const dateParts = value.split("/");
    const dateObject = new Date(
      +dateParts[2],
      +dateParts[1] - 1,
      +dateParts[0]
    );
    return dateObject;
  } catch {
    return null; // Or any default value you prefer
  }
};

export const dateTimeStringFormat = (value: number) => {
  const dateObject = new Date(value * 1000);
  const formattedDate = format(dateObject, "HH:mm, dd/MM/yyyy", { locale: vi });
  return formattedDate;
};

export const stringNumberToDate = (value: string) => {
  const seconds = +value;
  const dateObject = new Date(seconds * 1000);
  return dateObject;
};
