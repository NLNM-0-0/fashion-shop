import React from "react";
import { Button } from "./ui/button";
import {
  LuChevronLeft,
  LuChevronRight,
  LuChevronsLeft,
  LuChevronsRight,
} from "react-icons/lu";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import { usePagination } from "@/hooks/usePagination";

export interface PagingProps {
  page: string;
  totalPage: number;
}

const Paging = ({ page, totalPage }: PagingProps) => {
  const { goToNextPage, goBack, goToPage } = usePagination({
    totalPage: totalPage,
  });
  const pageArray = Array.from({ length: totalPage }, (value, key) => key + 1);

  return (
    <div className="flex gap-2">
      <Button
        variant="outline"
        size="icon"
        onClick={() => goToPage(1)}
        disabled={Number(page) <= 1}
      >
        <LuChevronsLeft className="h-4 w-4" />
      </Button>
      <Button
        variant="outline"
        size="icon"
        onClick={() => goBack()}
        disabled={Number(page) <= 1}
      >
        <LuChevronLeft className="h-4 w-4" />
      </Button>
      <Select
        value={page}
        onValueChange={(value) => {
          if (value != page) goToPage(parseInt(value));
        }}
      >
        <SelectTrigger className="w-[100px]">
          <SelectValue />
        </SelectTrigger>
        <SelectContent className="min-w-0">
          <SelectGroup>
            {pageArray.map((item) => {
              return (
                <SelectItem key={item} value={`${item}`}>
                  {`Page ${item}`}
                </SelectItem>
              );
            })}
          </SelectGroup>
        </SelectContent>
      </Select>
      <Button
        variant="outline"
        size="icon"
        onClick={() => goToNextPage()}
        disabled={Number(page) >= totalPage}
      >
        <LuChevronRight className="h-4 w-4" />
      </Button>
      <Button
        variant="outline"
        size="icon"
        onClick={() => goToPage(totalPage)}
        disabled={Number(page) >= totalPage}
      >
        <LuChevronsRight className="h-4 w-4" />
      </Button>
    </div>
  );
};

export default Paging;
