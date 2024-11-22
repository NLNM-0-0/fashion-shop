import { ApiError, BaseProduct } from "@/lib/types";
import { AspectRatio } from "../ui/aspect-ratio";
import Image from "next/image";
import { toVND } from "@/lib/utils";
import HeartFill from "@/lib/assets/icons/heart-fill.svg";
import { Button } from "../ui/button";
import { unlikeItem } from "@/lib/api/favorite/likeItem";
import { AxiosError } from "axios";
import { toast } from "@/hooks/use-toast";

const FavoriteProductItem = ({
  product,
  onMutate,
}: {
  product: BaseProduct;
  onMutate: () => void;
}) => {
  const handleUnLike = () => {
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
  };
  return (
    <div className="flex flex-col bg-white font-medium pb-10">
      <AspectRatio ratio={1 / 1} className="bg-muted">
        <Image
          src={product.images.at(0) ?? ""}
          alt="prd"
          fill
          className="h-full w-full rounded-md object-cover"
        />
      </AspectRatio>
      <div className="flex justify-between mt-3 items-start">
        <div>
          <div>{product.name}</div>
          <div>{toVND(product.unitPrice)}</div>
        </div>
        <Button
          variant={"ghost"}
          type="button"
          className="rounded-full"
          size={"icon"}
          onClick={handleUnLike}
        >
          <Image alt="fav" src={HeartFill.src} height={20} width={20} />
        </Button>
      </div>
    </div>
  );
};

export default FavoriteProductItem;
