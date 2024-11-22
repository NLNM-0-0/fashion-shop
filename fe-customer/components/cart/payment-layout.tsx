"use client";

import { useCartList } from "@/hooks/cart/useCartList";
import PaymentItem from "./payment-item";
import { toVND } from "@/lib/utils";
import { Input } from "../ui/input";
import GenderRadioButton from "../ui/gender-radio-button";
import { Button } from "../ui/button";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "../ui/accordion";
import { useEffect, useState } from "react";
import { z } from "zod";
import { phoneRegex, required } from "@/lib/helpers/zod";
import { SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createOrder } from "@/lib/api/order/createOrder";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { toast } from "@/hooks/use-toast";

const PaymentScheme = z.object({
  name: required,
  phone: z.string().regex(phoneRegex, "Invalid phone number"),
  address: required,
});

const PaymentLayout = () => {
  const { data, isLoading, error } = useCartList();
  const cartItems = data?.data.data;
  const totalValue = cartItems?.reduce(
    (total, item) => {
      return {
        totalPrice: total.totalPrice + item.quantity * item.item.unitPrice,
        totalQuantity: total.totalQuantity + item.quantity,
      };
    },
    { totalPrice: 0, totalQuantity: 0 }
  );

  const [isLargeScreen, setIsLargeScreen] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors, isDirty },
  } = useForm<z.infer<typeof PaymentScheme>>({
    resolver: zodResolver(PaymentScheme),
    defaultValues: {
      name: "",
      phone: "",
      address: "",
    },
  });

  const onSubmit: SubmitHandler<z.infer<typeof PaymentScheme>> = async (
    data
  ) => {
    createOrder({
      ...data,
      cardIds: cartItems ? cartItems?.map((item) => item.id) : [],
    })
      .then(() => {
        toast({
          variant: "success",
          title: "Success",
          description: "Place order successfully",
        });
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Place order failed",
        });
      });
  };

  useEffect(() => {
    const mediaQuery = window.matchMedia("(min-width: 1024px)");
    const handleResize = () => setIsLargeScreen(mediaQuery.matches);

    handleResize();

    mediaQuery.addEventListener("change", handleResize);
    return () => mediaQuery.removeEventListener("change", handleResize);
  }, []);

  if (error) return <>Failed to load</>;
  else if (!data || isLoading) return <>Skeleton...</>;
  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="flex gap-10 lg:flex-row flex-col-reverse w-full lg:max-w-[1000px] max-w-[620px]"
    >
      <div className="flex basis-2/3 flex-col">
        <div className="table___title mb-4">Contact information</div>
        <Input className="h-12 mb-1" placeholder="Name" {...register("name")} />
        {errors.name && (
          <span className="error___message">{errors.name.message}</span>
        )}
        <Input
          className="h-12 mt-5 mb-1"
          placeholder="Address"
          {...register("address")}
        />
        {errors.address && (
          <span className="error___message">{errors.address.message}</span>
        )}
        <Input
          className="h-12 mt-5 mb-1"
          placeholder="Phone Number"
          {...register("phone")}
        />
        {errors.phone && (
          <span className="error___message">{errors.phone.message}</span>
        )}
        <span className="text-xs mt-1 ml-4 text-fs-gray-darker">
          A carrier might contact you to confirm delivery
        </span>
        <div className="table___title mb-4 mt-10">Payment</div>
        <GenderRadioButton
          title="Cash on Delivery"
          value={true}
          selected
          onSelect={() => {}}
          readonly
          className="h-12"
        />
        <Button
          className="h-12 rounded-full mt-16"
          disabled={!isDirty || !cartItems || cartItems.length < 1}
        >
          Place order
        </Button>
      </div>

      <div className="flex basis-1/3 w-full flex-col self-start min-w-96">
        <Accordion
          type="single"
          value={isLargeScreen ? "item-1" : undefined}
          collapsible
          className="w-full"
        >
          <AccordionItem value="item-1" className="lg:border-none border-b">
            <AccordionTrigger className="no-underline hover:no-underline lg:[&>svg]:hidden [&>svg]:block lg:pointer-events-none pt-0">
              <div className="flex justify-between lg:items-start items-center w-full pr-2">
                <span className="table___title">Summary</span>
                <span className="sm:text-xl text-lg font-medium lg:hidden block">
                  {toVND(totalValue?.totalPrice ?? 0)} (
                  {totalValue?.totalQuantity} item
                  {totalValue?.totalQuantity ?? 0 > 1 ? "s" : ""})
                </span>
              </div>
            </AccordionTrigger>
            <AccordionContent>
              <div className="flex lg:flex-col-reverse flex-col">
                {data.data.data.length > 0 ? (
                  data.data.data.map((item) => (
                    <PaymentItem product={item} key={item.id} />
                  ))
                ) : (
                  <>There are no items to place order.</>
                )}
                <div>
                  <div className="flex justify-between text-base mt-5">
                    <span>Subtotal</span>
                    <span>{toVND(totalValue?.totalPrice ?? 0)}</span>
                  </div>
                  <div className="flex justify-between text-base mt-5">
                    <span>Estimated Delivery & Handling</span>
                    <span>Free</span>
                  </div>
                  <div className="flex justify-between text-base mt-5 py-4 border-t lg:border-b border-b-0 font-medium">
                    <span>Total</span>
                    <span>
                      <span>{toVND(totalValue?.totalPrice ?? 0)}</span>
                    </span>
                  </div>
                </div>
              </div>
            </AccordionContent>
          </AccordionItem>
        </Accordion>
      </div>
    </form>
  );
};

export default PaymentLayout;
