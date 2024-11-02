import type { Metadata } from "next";
import "@/lib/styles/globals.css";
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
      <body className={`${Helvetica.className}  antialiased flex h-screen`}>
        <>
          <main className="flex flex-1 w-full md:p-10 p-4 overflow-auto">
            {children}
            <Toaster />
          </main>
        </>
      </body>
    </html>
  );
}
