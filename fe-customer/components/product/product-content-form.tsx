import { ApiError, Product } from "@/lib/types";
import { getSizesFromColor, toVND } from "@/lib/utils";
import SizeRadioButton from "../ui/size-button";
import ColorItem from "./color-item";
import { z } from "zod";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "../ui/button";
import Heart from "@/lib/assets/icons/heart.svg";
import HeartFill from "@/lib/assets/icons/heart-fill.svg";

import Image from "next/image";
import { AxiosError } from "axios";
import { toast } from "@/hooks/use-toast";
import { addToCard } from "@/lib/api/cart/addToCart";
import { CART_NUMBER_KEY } from "@/hooks/cart/useCartNumber";
import { useSWRConfig } from "swr";
import { likeItem, unlikeItem } from "@/lib/api/favorite/likeItem";
import AddedToBagDialog from "../cart/added-to-bag-dialog";
import { useState } from "react";

export const ProductSchema = z.object({
  color: z.string().min(1, "Select a color"),
  size: z.string().min(1, "Select a size"),
});

const ProductContentForm = ({
  product,
  onMutate,
}: {
  product: Product;
  onMutate: () => void;
}) => {
  const { mutate } = useSWRConfig();
  const initialSizes = getSizesFromColor(product, product.colors.at(0)?.name);

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<z.infer<typeof ProductSchema>>({
    resolver: zodResolver(ProductSchema),
    defaultValues: {
      color: product.colors.at(0)?.name,
      size: initialSizes.length > 0 ? initialSizes.at(0)?.size : undefined,
    },
  });

  const sizeValue = watch("size");
  const colorValue = watch("color");
  const [added, setAdded] = useState(false);

  const onSubmit: SubmitHandler<z.infer<typeof ProductSchema>> = async (
    data
  ) => {
    addToCard({
      ...data,
      itemId: product.id,
      quantity: 1,
    })
      .then((res) => {
        if (res.data.data) {
          setAdded(true);
          mutate(CART_NUMBER_KEY);
        }
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Create account failed",
        });
      });
  };

  const handleLike = () => {
    if (product.liked) {
      unlikeItem(product.id)
        .then(() => onMutate())
        .catch((err: AxiosError<ApiError>) => {
          toast({
            variant: "destructive",
            title: "Error",
            description: err.response?.data.message ?? "Unlike product failed",
          });
          return;
        });
    } else
      likeItem(product.id)
        .then(() => onMutate())
        .catch((err: AxiosError<ApiError>) => {
          toast({
            variant: "destructive",
            title: "Error",
            description: err.response?.data.message ?? "Like product failed",
          });
          return;
        });
  };

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-6">
        <div>
          <h1 className="text-2xl tracking-wide font-medium">{product.name}</h1>
          <span className="font-medium text-base text-fs-gray-darker">
            {product.categories.map((item) => item.name).join(" | ")}
          </span>
        </div>
        <span className="font-medium text-base">
          {toVND(product.unitPrice)}
        </span>

        <Controller
          control={control}
          name={`color`}
          render={({ field }) => (
            <div className="flex gap-4 flex-wrap">
              {product.colors.map((color) => (
                <ColorItem
                  key={color.name}
                  color={color}
                  selected={field.value === color.name}
                  disable={product.quantities[`${sizeValue}-${color.name}`] < 1}
                  onSelected={(value) => {
                    field.onChange(value);
                  }}
                />
              ))}
            </div>
          )}
        />
        {errors.color && (
          <span className="error___message">{errors.color.message}</span>
        )}
        <div className="mt-14 flex flex-col gap-2">
          <span className="font-medium text-base">Select Size</span>
          <Controller
            control={control}
            name={`size`}
            render={({ field }) => (
              <div className="flex flex-row flex-wrap gap-2">
                {product.sizes.map((size) => (
                  <SizeRadioButton
                    key={size.name}
                    value={size.name}
                    selected={field.value === size.name}
                    className="px-6 min-w-20"
                    onSelect={(value) => field.onChange(value)}
                    readonly={
                      product.quantities[`${size.name}-${colorValue}`] < 1
                    }
                  />
                ))}
              </div>
            )}
          />
          {errors.size && (
            <span className="error___message">{errors.size.message}</span>
          )}
        </div>
        <div className="flex flex-col gap-3">
          <Button className="rounded-full w-full h-14">Add to Bag</Button>
          <Button
            type="button"
            className="rounded-full w-full h-14"
            size={"lg"}
            variant={"outline"}
            onClick={handleLike}
          >
            Favorite
            {product.liked ? (
              <Image src={HeartFill.src} alt="heart" height={20} width={20} />
            ) : (
              <Image src={Heart.src} alt="heart" height={20} width={20} />
            )}
          </Button>
        </div>
      </form>
      <AddedToBagDialog
        open={added}
        onOpenChange={setAdded}
        product={{ item: product, size: sizeValue, color: colorValue }}
      />
    </>
  );
};

export default ProductContentForm;
