"use client";
import loadingEmitter from "@/lib/api/loading-emitter";
import { useEffect, useState } from "react";

export const LoadingSpinner = () => {
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const handleLoading = (loading: boolean) => {
      setIsLoading(loading);
    };

    loadingEmitter.on("loading", handleLoading);

    return () => {
      loadingEmitter.off("loading", handleLoading);
    };
  }, []);
  return isLoading ? (
    <div className="fixed top-0 left-0 w-full h-full  flex justify-center bg-[rgba(240,240,240,0.35)] items-center z-[999]">
      <div className="loader"></div>
    </div>
  ) : null;
};
