import { FilterParams } from "@/hooks/useFilterList";
import { FormFilterType, FormFilterValues } from "@/lib/types";
import FilterPopover from "./filter-popover";
import { FilterInputType } from "@/lib/constants/enum";
import { stringNumberToDate } from "@/lib/helpers/date";
import { format } from "date-fns";
import { vi } from "date-fns/locale";

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
          if (!filterItem?.type) {
            return null;
          } else
            return (
              <div
                key={key}
                className="rounded-xl flex self-start px-3 py-2 h-fit outline-none text-sm text-primary bg-gray-200 items-center gap-1 group"
              >
                <span>
                  {filterItem?.inputType !== FilterInputType.BOOLEAN && (
                    <>
                      {filterItem?.title}
                      {": "}
                    </>
                  )}

                  {filterItem?.inputType === FilterInputType.BOOLEAN
                    ? value === "true"
                      ? filterItem.trueTitle
                      : filterItem.falseTitle
                    : filterItem?.inputType === FilterInputType.DATE
                    ? format(
                        stringNumberToDate((value ?? "").toString()),
                        "dd/MM/yyyy",
                        {
                          locale: vi,
                        }
                      )
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
