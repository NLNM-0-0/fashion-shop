import { Gender } from "@/lib/constants/enum";
import { StaticImageData } from "next/image";
import MenImage from "@/lib/assets/images/men.png";
import WomenImage from "@/lib/assets/images/women.png";
import KidsImage from "@/lib/assets/images/kids.png";
import Image from "next/image";
import { Button } from "../ui/button";
import { useRouter } from "next/navigation";
import encodeParams from "@/lib/helpers/params";

interface EssentialItem {
  image: StaticImageData;
  title: string;
  gender: Gender;
}

const items: EssentialItem[] = [
  {
    image: MenImage,
    title: "Men's",
    gender: Gender.MEN,
  },
  {
    image: WomenImage,
    title: "Women's",
    gender: Gender.WOMEN,
  },
  {
    image: KidsImage,
    title: "Kid's",
    gender: Gender.BOYS,
  },
];

const EssentialList = () => {
  const router = useRouter();
  return (
    <div className="flex flex-col gap-6">
      <span className="text-xl font-medium">The Essentials</span>
      <div className="flex gap-3">
        {items.map((item) => (
          <div key={item.gender} className="aspect-[440/540] flex-1 relative">
            <Image
              src={item.image.src}
              alt={item.gender}
              height={500}
              width={400}
              className="h-full w-full object-cover"
            ></Image>
            <Button
              variant={"outline"}
              className="absolute xl:left-12 xl:bottom-12 left-6 bottom-6 rounded-full border-none hover:bg-gray-200"
              onClick={() => {
                const queryParams = encodeParams({ genders: item.gender });
                router.push(`/fa/products?${queryParams}`);
              }}
            >
              {item.title}
            </Button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EssentialList;
