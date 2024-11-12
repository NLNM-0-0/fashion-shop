import Link from "next/link";
import Image from "next/image";

const LogoButton = () => {
  return (
    <Link href="/">
      <div className="p-2">
        <Image
          src="/android-chrome-192x192.png"
          alt="logo"
          width={50}
          height={50}
        ></Image>
      </div>
    </Link>
  );
};

export default LogoButton;
