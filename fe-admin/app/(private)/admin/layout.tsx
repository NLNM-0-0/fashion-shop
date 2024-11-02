import type { Metadata } from "next";
import "@/lib/styles/globals.css";
import Sidebar from "@/components/sidebar";
import HeaderMobile from "@/components/header-mobile";
import { Helvetica } from "@/lib/fonts";
import { Toaster } from "@/components/ui/toaster";

export const metadata: Metadata = {
  title: "Fashion Admin",
  description: "Fashion Admin Website",
  icons: {
    icon: ["/favicon.ico?v=4"],
    apple: ["/apple-touch-icon.png?v=4"],
    shortcut: ["apple-touch-icon.png"],
  },
  manifest: "/site.webmanifest",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${Helvetica.className}  antialiased flex h-full`}>
        <>
          <Sidebar />
          <main className="flex flex-1">
            <div className="flex w-full flex-col overflow-y-hidden">
              {/* <Header /> */}
              <HeaderMobile />
              <div className="md:p-10 p-4 md:mt-0 mt-12 overflow-auto">
                {children}
              </div>
              <Toaster />
            </div>
          </main>
        </>
      </body>
    </html>
  );
}
