import type { Metadata } from "next";
import "@/lib/styles/globals.css";
import { Helvetica } from "@/lib/fonts";

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

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en h-full">
      <body
        className={`${Helvetica.className}  antialiased flex h-full overflow-hidden`}
      >
        {children}
      </body>
    </html>
  );
}
