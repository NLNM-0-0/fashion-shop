"use client";

import { SWRConfig } from "swr";
import Cookies from "js-cookie";

export default function SWRProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <SWRConfig
      value={{
        onError: (error) => {
          if (error.response?.status === 401) {
          }
          if (error.status === 401) {
            Cookies.remove("token");
            window.location.href = "/login";
          }
        },
        revalidateOnFocus: false,
      }}
    >
      {children}
    </SWRConfig>
  );
}
