import { useEffect, useState } from "react";
import {
  Carousel,
  CarouselApi,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "../ui/carousel";
import Image from "next/image";
import ImageButton from "./image-button";

const ProductImage = ({ images }: { images: string[] }) => {
  const [api, setApi] = useState<CarouselApi>();
  const [current, setCurrent] = useState(0);

  const goToSlide = (index: number) => {
    api?.scrollTo(index);
  };

  useEffect(() => {
    if (!api) {
      return;
    }

    setCurrent(api.selectedScrollSnap());

    api.on("select", () => {
      setCurrent(api.selectedScrollSnap());
    });
  }, [api]);

  return (
    <div className="flex sm:flex-row flex-col-reverse md:gap-9 gap-6">
      <div className="flex sm:flex-col flex-row flex-wrap gap-3">
        {images.map((item, index) => (
          <ImageButton
            key={`prd-image-${index}`}
            image={item}
            selected={current === index}
            onClick={() => goToSlide(index)}
          />
        ))}
      </div>
      <Carousel
        className="md:w-[600px] sm:w-[500px] md:h-[700px] sm:h-[580px] relative rounded-2xl overflow-clip"
        opts={{
          loop: true,
        }}
        setApi={setApi}
      >
        <CarouselContent>
          {images.map((img) => (
            <CarouselItem key={`product-image-${img}`}>
              <Image
                alt="Header 1"
                className="object-cover w-full md:h-[700px]"
                priority
                src={img}
                width={600}
                height={700}
              />
            </CarouselItem>
          ))}
        </CarouselContent>
        <div className="absolute right-12 bottom-10 flex gap-3">
          <CarouselPrevious />
          <CarouselNext />
        </div>
      </Carousel>
    </div>
  );
};

export default ProductImage;
