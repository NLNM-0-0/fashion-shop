import { Category } from "@/lib/types";
import React from "react";
import { Button } from "../ui/button";

interface CategoryListProps {
  categories: Category[];
  onClick: (categoryId: number) => void;
}
const CategoryList = ({ categories, onClick }: CategoryListProps) => {
  return (
    <div className="grid md:grid-cols-4 grid-cols-3 gap-2">
      {categories.map((item) => (
        <Button
          variant={"link"}
          key={item.id}
          className="text-fs-gray-dark hover:text-fs-black pl-[30%] no-underline hover:no-underline"
          onClick={() => onClick(item.id)}
        >
          <span className="text-sm font-bold transition-colors">
            {item.name}
          </span>
        </Button>
      ))}
    </div>
  );
};

export default CategoryList;
