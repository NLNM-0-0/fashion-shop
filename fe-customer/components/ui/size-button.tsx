import { cn } from "@/lib/utils";
import { Button } from "./button";

interface SizeRadioButtonProps {
  value: string;
  selected: boolean;
  className?: string;
  onSelect: (value: string) => void;
  readonly?: boolean;
}
const SizeRadioButton = ({
  value,
  selected,
  className,
  readonly,
  onSelect,
}: SizeRadioButtonProps) => {
  return (
    <Button
      type="button"
      variant="outline"
      disabled={readonly}
      className={cn(
        "text-fs-gray-dark",
        className,
        selected && "border-fs-black text-fs-black"
      )}
      onClick={() => onSelect(value)}
    >
      {value}
    </Button>
  );
};

export default SizeRadioButton;
