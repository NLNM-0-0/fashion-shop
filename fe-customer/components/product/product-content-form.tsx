import { Product } from "@/lib/types";
import { toVND } from "@/lib/utils";
import SizeRadioButton from "../ui/size-button";
import ColorItem from "./color-item";

const ProductContentForm = ({ product }: { product: Product }) => {
  return (
    <div className="flex flex-col gap-6">
      <div>
        <h1 className="text-2xl tracking-wide font-medium">{product.name}</h1>
        <span className="font-medium text-sm">
          {product.categories.map((item) => item.name).join(", ")}
        </span>
      </div>
      <span className="font-medium text-base">{toVND(product.price)}</span>
      <div className="flex gap-4 flex-wrap">
        {product.colors.map((color) => (
          <ColorItem
            key={color.name}
            color={color}
            selected
            disable
            onSelected={(value) => {
              console.log(value);
            }}
          />
        ))}
      </div>
      <div className="mt-14 flex flex-col gap-2">
        <span className="font-medium text-base">Select Size</span>
        <div className="grid grid-cols-3 gap-2">
          {product.sizes.map((size) => (
            <SizeRadioButton
              key={size.name}
              value={size.name}
              selected
              onSelect={(value) => console.log(value)}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductContentForm;
