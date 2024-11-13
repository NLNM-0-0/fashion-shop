import { cn } from "@/lib/utils";
import { Button } from "./button";

interface GenderRadioButtonProps {
  title: string;
  value: boolean;
  selected: boolean;
  className?: string;
  onSelect: (value: boolean) => void;
  readonly?: boolean;
}
const GenderRadioButton = ({
  title,
  value,
  selected,
  className,
  readonly,
  onSelect,
}: GenderRadioButtonProps) => {
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
      {title}
    </Button>
  );
};

export default GenderRadioButton;
