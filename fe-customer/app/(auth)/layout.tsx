import type { Metadata } from "next";
import "@/lib/styles/globals.css";
import { Toaster } from "@/components/ui/toaster";
import { LoadingSpinner } from "@/components/loading-spinner";

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

export default function AuthLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <LoadingSpinner />

      <main className="flex flex-1 h-screen w-full md:p-10 p-4 overflow-auto">
        {children}
        <Toaster />
      </main>
    </>
  );
}
