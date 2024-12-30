import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
const authRoutes = ["/reset-password", "/signup", "/login", "/otp"];

export function middleware(request: NextRequest) {
  const token = request.cookies.get("token")?.value;
  const url = request.nextUrl.clone();

  const publicRoutes = [
    "/login",
    "/otp",
    "/reset-password",
    "/signup",
    "/fa",
    "/fa/products",
    "/fa/products/:id",
  ];

  if (token && authRoutes.includes(url.pathname)) {
    return NextResponse.redirect(new URL("/fa", request.url));
  }

  if (
    publicRoutes.includes(url.pathname) ||
    url.pathname.startsWith("/fa/products/")
  ) {
    return NextResponse.next();
  }

  if (!token) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return NextResponse.next();
}
export const config = {
  matcher: ["/((?!api|static|.*\\..*|_next).*)"],
};
