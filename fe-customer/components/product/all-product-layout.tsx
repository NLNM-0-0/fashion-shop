"use client";
import { useState } from "react";
import Filter from "./filter";
import FilterBySelect from "./filter-by-select";
import { useProductList } from "@/hooks/useProductList";
import { SortType } from "@/lib/constants/enum";
import ProductItem from "./product-item";
import FilterSheet from "./filter-sheet";
import { Button } from "../ui/button";
import { PiSlidersHorizontal } from "react-icons/pi";
import { cn } from "@/lib/utils";
import FavoriteListSkeleton from "../favorite/favorite-list-skeleton";
import Paging from "../paging";

const AllProductLayout = () => {
  const { filters, updateFilterUrl, data, isLoading, error } = useProductList();
  const [selectedSortType, setSelectedSortType] = useState<string | null>(null);
  const [showFilter, setShowFilter] = useState(true);

  const productList = data?.data.data;

  const onSelectedSortTypeChange = (sortType: string | null) => {
    setSelectedSortType(sortType);
    switch (sortType) {
      case SortType.Newest:
        updateFilterUrl({ sortNew: "true", sortPrice: null });
        break;
      case SortType.Oldest:
        updateFilterUrl({ sortNew: "false", sortPrice: null });
        break;
      case SortType.PriceHighLow:
        updateFilterUrl({ sortNew: null, sortPrice: "true" });
        break;
      case SortType.PriceLowHigh:
        updateFilterUrl({ sortNew: null, sortPrice: "false" });
        break;
    }
  };
  if (error) return <>Failed to load...</>;
  return (
    <div className="flex flex-row overflow-hidden gap-10">
      <div
        className={cn(
          "w-[260px] h-full",
          !showFilter && "hidden",
          showFilter && "lg:block hidden"
        )}
      >
        <Filter
          className="flex fixed flex-col sm:top-[74px] top-[114px] bottom-0 w-[260px] mt-10"
          filters={filters}
          updateFilters={updateFilterUrl}
        />
      </div>
      <div className="flex-1 flex flex-col ">
        <div className="flex gap-2 items-center">
          <span className="capitalize flex-1 md:text-lg text-base font-medium">
            {filters.genders
              ? Array.isArray(filters.genders)
                ? filters.genders.join(", ").toLowerCase()
                : filters.genders.toLowerCase()
              : ""}
            {filters.genders && filters.categoryName ? " | " : ""}
            {filters.categoryName
              ? decodeURIComponent(filters.categoryName.toString())
              : ""}
          </span>
          <FilterBySelect
            selectedValue={selectedSortType}
            setSelectedValue={onSelectedSortTypeChange}
          />
          <div className="lg:hidden block">
            <FilterSheet filters={filters} updateFilters={updateFilterUrl} />
          </div>
          <Button
            type="button"
            onClick={() => setShowFilter((prev) => !prev)}
            variant="outline"
            className="lg:flex hidden rounded-full gap-1"
          >
            {showFilter ? "Hide" : "Show"} Filter
            <PiSlidersHorizontal className="!w-6 !h-6" />
          </Button>
        </div>
        {!data || isLoading ? (
          <>
            <FavoriteListSkeleton />
          </>
        ) : (
          <>
            {productList && productList?.length < 1 && (
              <>There are no items found.</>
            )}

            <div className="grid lg:grid-cols-3 grid-cols-2 md:gap-4 gap-2 md:px-4 pt-6">
              {productList?.map((item) => (
                <ProductItem key={item.id} product={item} />
              ))}
            </div>
            <div className="w-full flex justify-end">
              <Paging
                page={data?.data.page.index.toString() ?? "1"}
                totalPage={data?.data.page.totalPages ?? 1}
              />
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default AllProductLayout;
