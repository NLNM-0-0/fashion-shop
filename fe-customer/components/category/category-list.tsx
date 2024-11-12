import { Category } from "@/lib/types";
import Link from "next/link";
import React from "react";

const CategoryList = ({ categories }: { categories: Category[] }) => {
  return (
    <div className="grid md:grid-cols-4 grid-cols-3 gap-2">
      {categories.map((item) => (
        <div
          key={item.id}
          className="text-fs-gray-dark hover:text-fs-black text-sm font-bold transition-colors pl-[30%]"
        >
          <Link href={"#"}>{item.name}</Link>
        </div>
      ))}
    </div>
  );
};

export default CategoryList;
