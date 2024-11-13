import { Button } from "../ui/button";
import { useEffect, useState } from "react";
import DaypickerPopup from "../ui/daypicker-popup";
import { stringNumberToDate } from "@/lib/helpers/date";
import { FilterParams } from "@/hooks/useFilterList";

export interface ReportFilterValue {
  timeFrom: string;
  timeTo: string;
}

export interface ReportFilterProps {
  filters: FilterParams;
  onApplyFilters: (data: ReportFilterValue) => void;
}
const ReportFilter = ({ filters, onApplyFilters }: ReportFilterProps) => {
  const [timeFrom, setTimeFrom] = useState<string>();
  const [timeTo, setTimeTo] = useState<string>();

  useEffect(() => {
    setTimeFrom(filters.timeFrom);
    setTimeTo(filters.timeTo);
  }, [filters]);

  return (
    <div className="flex flex-row gap-4 py-4">
      <div>
        <span className="font-medium">From Date</span>
        <DaypickerPopup
          triggerClassname="flex-1 w-full mt-1"
          date={timeFrom ? stringNumberToDate(timeFrom) : new Date()}
          setDate={(date) => {
            const seconds = date?.valueOf();
            setTimeFrom(`${(seconds ?? 0) / 1000}`);
          }}
        />
      </div>
      <div>
        <span className="font-medium mb-2">To Date</span>
        <DaypickerPopup
          triggerClassname="flex-1 w-full mt-1"
          date={timeTo ? stringNumberToDate(timeTo) : new Date()}
          setDate={(date) => {
            const seconds = date?.valueOf();
            setTimeTo(`${(seconds ?? 0) / 1000}`);
          }}
        />
      </div>
      <Button
        type="submit"
        className="self-end"
        onClick={() => {
          onApplyFilters({ timeFrom: timeFrom ?? "", timeTo: timeTo ?? "" });
        }}
      >
        View
      </Button>
    </div>
  );
};

export default ReportFilter;
