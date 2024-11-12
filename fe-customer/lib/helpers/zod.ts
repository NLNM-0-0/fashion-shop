import * as z from "zod";

export const required = z.string().min(1, "This field is required.");
export const phoneRegex = new RegExp(/(0[3|5|7|8|9])+([0-9]{8})\b/g);

export const passwordMinError = "Please enter at least 6 characters";
