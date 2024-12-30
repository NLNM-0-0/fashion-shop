import { useCategoryList } from "@/hooks/useCategory";
import { Button } from "../ui/button";
import { useProductFilter } from "./product-filter-context";
import { cn } from "@/lib/utils";

const CategoryFilter = () => {
  const { filters, updateFilterUrl } = useProductFilter();
  const { data, isLoading, error } = useCategoryList();

  if (error) return <>Failed to load</>;
  if (isLoading || !data) {
    return <>Skeleton...</>;
  } else
    return (
      <div className="flex flex-col pb-4 border-b">
        {data.data.data.map((item) => (
          <Button
            variant={"link"}
            key={item.id}
            type="button"
            className={cn(
              "hover:text-fs-gray-dark text-fs-black transition-colors hover:no-underline justify-start px-0",
              item.id.toString() == filters.categoryId && "!font-bold"
            )}
            onClick={() => {
              void updateFilterUrl({
                categoryId: item.id.toString(),
                categoryName: item.name.toString(),
              });
            }}
          >
            {item.name}
          </Button>
        ))}
      </div>
    );
};

export default CategoryFilter;
