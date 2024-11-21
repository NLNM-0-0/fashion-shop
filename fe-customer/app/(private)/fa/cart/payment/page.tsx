import PaymentLayout from "@/components/cart/payment-layout";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Payment",
};

const Payment = () => {
  return (
    <div className="flex justify-center">
      <PaymentLayout />
    </div>
  );
};

export default Payment;
