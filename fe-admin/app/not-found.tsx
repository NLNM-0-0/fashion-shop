"use client";

import { Player } from "@lottiefiles/react-lottie-player";
import "@/lib/styles/globals.css";

const NotFoundPage = () => {
  return (
    <div className="flex flex-col items-center mt-[15vh] gap-16 w-full">
      <Player
        autoplay
        loop
        src="/not-found-animation.json"
        style={{ width: "80%" }}
      />
      <h1 className="text-3xl lg:text-4xl font-bold">
        Oops! Page not found
      </h1>
    </div>
  );
};

export default NotFoundPage;
