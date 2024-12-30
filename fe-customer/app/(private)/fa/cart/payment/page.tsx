import { AuthProvider } from "@/components/auth/auth-context";
import SWRProvider from "@/components/auth/swr-provider";
import PaymentLayout from "@/components/cart/payment-layout";
import PaymentSkeleton from "@/components/cart/payment-skeleton";
import { Metadata } from "next";
import { Suspense } from "react";

export const metadata: Metadata = {
  title: "Payment",
};

const Payment = () => {
  return (
    <div className="flex justify-center">
      <Suspense fallback={<PaymentSkeleton />}>
        <SWRProvider>
          <AuthProvider>
            <PaymentLayout />
          </AuthProvider>
        </SWRProvider>
      </Suspense>
    </div>
  );
};

export default Payment;
