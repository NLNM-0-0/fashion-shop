"use client";
import { cn } from "@/lib/utils";
import { Button } from "./button";

interface StatusButtonProps {
  title: string;
  value: string;
  selected: boolean;
  className?: string;
  onSelect?: (value: string) => void;
}
const StatusButton = ({
  title,
  value,
  selected,
  className,
  onSelect,
}: StatusButtonProps) => {
  return (
    <Button
      style={{ fontWeight: 500 }}
      onClick={() => onSelect?.(value)}
      variant={"ghost"}
      type="button"
      className={cn(
        "px-6 border-b-2 border-transparent border-solid bg-transparent hover:bg-transparent rounded-none",
        selected &&
          "border-b-2 border-fs-black bg-transparent hover:bg-transparent font-bold",
        className
      )}
    >
      {title}
    </Button>
  );
};

export default StatusButton;
