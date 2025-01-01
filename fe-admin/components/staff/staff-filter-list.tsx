"use client";

import { useStaffList } from "@/hooks/useStaffList";
import { Staff } from "@/lib/types";
import React, { useEffect, useState } from "react";
import { LuCheck, LuChevronsUpDown } from "react-icons/lu";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";
import { Button } from "../ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "../ui/command";
import { cn } from "@/lib/utils";
import { Skeleton } from "../ui/skeleton";
import { useAuth } from "../auth/auth-context";

export interface StaffListProps {
  staff: string | number;
  setStaff: (staff: string | number, name?: string) => void;
  isId: boolean;
  readonly?: boolean;
}
const StaffList = ({ isId, staff, setStaff, readonly }: StaffListProps) => {
  const { user } = useAuth();
  const [open, setOpen] = useState(false);
  const { data, error, isLoading } = useStaffList(user);
  const [staffList, setStaffList] = useState<Array<Staff>>([]);
  useEffect(() => {
    if (data) {
      setStaffList(data.data.data);
    }
  }, [data]);

  if (error) return <div>Failed to load</div>;
  if (isLoading || !data) {
    return <Skeleton className="h-10 w-full" />;
  } else {
    return (
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button
            disabled={readonly}
            variant="outline"
            role="combobox"
            aria-expanded={open}
            className="justify-between w-full min-w-0 h-10"
          >
            {staff && staff !== -1
              ? staffList.find(
                  (item) =>
                    (isId && item.id.toString() === staff.toString()) ||
                    (!isId && item.name === staff)
                )?.name
              : "Choose staff"}
            <LuChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="PopoverContent rounded-xl w-full">
          <Command className="w-full">
            <CommandInput placeholder="Find staff's name" />
            <CommandEmpty className="py-2 px-6">
              <div className="text-sm">Not found</div>
            </CommandEmpty>
            <CommandList>
              <CommandGroup>
                {staffList.map((item) => (
                  <CommandItem
                    value={item.name}
                    key={item.id}
                    onSelect={() => {
                      if (isId) {
                        setStaff(item.id, item.name);
                      } else {
                        setStaff(item.name, item.name);
                      }

                      setOpen(false);
                    }}
                  >
                    <LuCheck
                      className={cn(
                        "mr-2 h-4 w-4",
                        isId
                          ? item.id.toString() === staff.toString()
                            ? "opacity-100"
                            : "opacity-0"
                          : item.name === staff
                          ? "opacity-100"
                          : "opacity-0"
                      )}
                    />
                    {item.name}
                  </CommandItem>
                ))}
              </CommandGroup>
            </CommandList>
          </Command>
        </PopoverContent>
      </Popover>
    );
  }
};

export default StaffList;
