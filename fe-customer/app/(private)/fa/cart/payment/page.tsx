import { AuthProvider } from "@/components/auth/auth-context";
import PaymentLayout from "@/components/cart/payment-layout";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Payment",
};

const Payment = () => {
  return (
    <div className="flex justify-center">
      <AuthProvider>
        <PaymentLayout />
      </AuthProvider>
    </div>
  );
};

export default Payment;
