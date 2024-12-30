import React from "react";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "../ui/carousel";
import Image from "next/image";
import { Product } from "@/lib/types";
import { toVND } from "@/lib/utils";
import Link from "next/link";

interface ProductCarouselProps {
  products?: Product[];
  title?: string;
}
const ProductCarousel = ({ products, title }: ProductCarouselProps) => {
  return (
    <div className="w-full">
      <Carousel
        opts={{
          align: "start",
        }}
        className="w-full"
      >
        <div className="flex justify-end mb-3 gap-3 items-center">
          <span className="text-xl font-medium flex-1">{title}</span>
          <CarouselPrevious className="bg-fs-gray-light hover:bg-fs-gray-lighter hover:text-fs-gray-darker disabled:text-fs-gray-dark h-12 w-12" />
          <CarouselNext className="bg-fs-gray-light hover:bg-fs-gray-lighter hover:text-fs-gray-darker disabled:text-fs-gray-dark h-12 w-12" />
        </div>
        <CarouselContent className="-ml-5">
          {products?.map((prd, index) => (
            <CarouselItem
              key={`product-${prd.id}-${index}`}
              className="lg:basis-1/3 sm:basis-1/2 basis-full pl-5"
            >
              <div className="flex flex-col w-full pb-10">
                <div className="aspect-square">
                  <Link href={`/fa/products/${prd.id}`}>
                    <Image
                      src={prd.images.at(0) ?? ""}
                      alt="prd"
                      width={400}
                      height={400}
                      className="w-full h-auto object-cover aspect-square"
                    />
                  </Link>
                </div>
                <Link
                  href={`/fa/products/${prd.id}`}
                  className="font-medium mt-3"
                >
                  {prd.name}
                </Link>
                <span className="text-fs-gray-darker font-normal">
                  {prd.categories?.map((item) => item.name).join(" | ")}
                </span>
                <span className="font-medium mt-2a">
                  {toVND(prd.unitPrice)}
                </span>
              </div>
            </CarouselItem>
          ))}
        </CarouselContent>
      </Carousel>
    </div>
  );
};

export default ProductCarousel;
