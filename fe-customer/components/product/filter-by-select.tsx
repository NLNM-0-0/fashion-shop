"use client";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { SortType } from "@/lib/constants/enum";

interface FilterBySelectProps {
  selectedValue: string | null;
  setSelectedValue: (value: string | null) => void;
}
const FilterBySelect = ({
  selectedValue,
  setSelectedValue,
}: FilterBySelectProps) => {
  return (
    <Select onValueChange={setSelectedValue}>
      <SelectTrigger className="w-auto border-none outline-none text-base">
        <SelectValue placeholder="Sort By">
          Sort By:
          <span className="ml-1 text-fs-gray-darker">{selectedValue}</span>
        </SelectValue>
      </SelectTrigger>
      <SelectContent className="w-auto">
        <SelectGroup className="text-bae">
          {Object.values(SortType).map((item) => (
            <SelectItem key={item} value={item}>
              {item}
            </SelectItem>
          ))}
        </SelectGroup>
      </SelectContent>
    </Select>
  );
};

export default FilterBySelect;
