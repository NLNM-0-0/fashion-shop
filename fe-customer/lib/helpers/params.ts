import { NotiFilterParam } from "../types";

type EncodableParams =
  | NotiFilterParam
  | { [key: string]: string | number | boolean | string[] };

export default function encodeParams(
  params: EncodableParams,
  hasPaging = true,
  prefix = ""
) {
  const searchParams = new URLSearchParams();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  function addParam(key: string, value: any) {
    if (Array.isArray(value)) {
      value.forEach((val) => searchParams.append(key, String(val)));
    } else if (value !== undefined && value !== null) {
      searchParams.append(key, encodeURIComponent(value));
    }
  }

  const { page = 1, limit = 10, ...otherParams } = params;

  if (hasPaging) {
    addParam("page", page);
    addParam("limit", limit);
  }

  Object.entries(otherParams).forEach(([key, value]) =>
    addParam(prefix ? `${prefix}[${key}]` : key, value)
  );

  return searchParams;
}
