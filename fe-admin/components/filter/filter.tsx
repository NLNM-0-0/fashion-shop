import { FilterParams } from "@/hooks/useFilterList";
import { FormFilterType, FormFilterValues } from "@/lib/types";
import FilterPopover from "./filter-popover";
import { FilterInputType } from "@/lib/constants/enum";

export interface FilterPopoverProps {
  title: string;
  filters: FilterParams;
  filterValues: FormFilterType[];
  onApplyFilters: (data: FormFilterValues) => void;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}
const Filter = ({ ...props }: FilterPopoverProps) => {
  return (
    <div className="flex items-start py-4 gap-2">
      <div className="flex gap-2">
        <FilterPopover {...props} />
      </div>
      <div className="flex flex-wrap gap-2">
        {Object.entries(props.filters).map(([key, value]) => {
          const filterItem = props.filterValues.find((v) => v.type === key);
          return (
            <div
              key={key}
              className="rounded-xl flex self-start px-3 py-2 h-fit outline-none text-sm text-primary bg-gray-200 items-center gap-1 group"
            >
              <span>
                {filterItem?.title}
                {": "}
                {filterItem?.inputType === FilterInputType.GENDER
                  ? value === "true"
                    ? "Male"
                    : "Female"
                  : value}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Filter;
