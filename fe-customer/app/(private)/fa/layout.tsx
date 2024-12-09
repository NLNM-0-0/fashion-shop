import type { Metadata } from "next";
import "@/lib/styles/globals.css";
import { Toaster } from "@/components/ui/toaster";
import SWRProvider from "@/components/auth/swr-provider";
import { AuthProvider } from "@/components/auth/auth-context";
import { LoadingSpinner } from "@/components/loading-spinner";
import Header from "@/components/header";
import { ProductFilterProvider } from "@/components/product/product-filter-context";

export const metadata: Metadata = {
  title: "Fashop",
  description: "A Fashion Website",
  icons: {
    icon: ["/favicon.ico?v=4"],
    apple: ["/apple-touch-icon.png?v=4"],
    shortcut: ["apple-touch-icon.png"],
  },
  manifest: "/site.webmanifest",
};

export default function PrivateLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="flex flex-1">
      <LoadingSpinner />
      <main className="flex flex-1 h-screen">
        <div className="flex w-full flex-col overflow-y-hidden">
          <ProductFilterProvider>
            <>
              <SWRProvider>
                <AuthProvider>
                  <Header />
                </AuthProvider>
              </SWRProvider>
              <div className="md:p-10 p-6 overflow-auto">{children}</div>
            </>
          </ProductFilterProvider>
          <Toaster />
        </div>
      </main>
    </div>
  );
}
