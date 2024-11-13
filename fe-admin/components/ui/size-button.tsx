import { cn } from "@/lib/utils";
import { Button } from "./button";

interface SizeRadioButtonProps {
  name: string;
  selected: boolean;
  className?: string;
  readonly?: boolean;
  onSelect?: (value: string) => void;
}
const SizeRadioButton = ({
  name,
  selected,
  className,
  readonly,
  onSelect,
}: SizeRadioButtonProps) => {
  return (
    <Button
      type="button"
      variant="outline"
      className={cn(
        "text-fs-gray-dark",
        className,
        selected && "border-fs-black capitalize text-fs-black",
        readonly && "pointer-events-none"
      )}
      onClick={() => onSelect?.(name)}
    >
      {name}
    </Button>
  );
};

export default SizeRadioButton;
