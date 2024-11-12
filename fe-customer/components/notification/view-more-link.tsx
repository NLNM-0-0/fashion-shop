import Link from "next/link";
import { useRouter } from "next/router";
import React from "react";
import { GoArrowRight } from "react-icons/go";

const ViewMoreLink = () => {
  const router = useRouter();
  const { limit, ...restParams } = router.query;

  const searchParams = new URLSearchParams();
  for (const key in restParams) {
    const value = restParams[key];
    if (Array.isArray(value)) {
      value.forEach((val) => searchParams.append(key, val));
    } else if (typeof value === "string") {
      searchParams.set(key, value);
    }
  }
  const currentLimit = Number(limit) || 0;
  searchParams.set("limit", String(currentLimit + 10));

  return (
    <Link
      href={`?${searchParams.toString()}`}
      className="uppercase text-xs font-bold hover:text-primary transition-colors tracking-wider mt-5 flex flex-row items-center gap-2"
    >
      View more
      <GoArrowRight className="h-4 w-4" />
    </Link>
  );
};

export default ViewMoreLink;
