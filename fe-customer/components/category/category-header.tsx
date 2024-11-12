import React from "react";
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuList,
  NavigationMenuTrigger,
} from "../ui/navigation-menu";
import { Gender } from "@/lib/constants/enum";
import CategoryList from "./category-list";
import { useCategoryList } from "@/hooks/useCategory";

const CategoryHeader = () => {
  const { data, isLoading, error } = useCategoryList();
  const genderList = Object.values(Gender);

  if (error) return <>Failed to load</>;
  if (isLoading || !data) {
    return <div className="flex-1"></div>;
  }
  return (
    <div className="flex-1 flex justify-center">
      <NavigationMenu>
        <NavigationMenuList>
          {genderList.map((gender) => (
            <NavigationMenuItem key={gender}>
              <NavigationMenuTrigger>
                <span className="uppercase text-sm"> {gender}</span>
              </NavigationMenuTrigger>
              <NavigationMenuContent>
                <div className="w-screen bg-white xl:px-80 md:px-40 px-10 md:py-12 py-8">
                  <CategoryList categories={data?.data.data} />
                </div>
              </NavigationMenuContent>
            </NavigationMenuItem>
          ))}
        </NavigationMenuList>
      </NavigationMenu>
    </div>
  );
};

export default CategoryHeader;