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

const PaymentLayout = () => {
  const { data, isLoading, error } = useCartList();
  const price = data?.data.data.reduce(
    (total, item) => total + item.item.unitPrice,
    0
  );

  const [isLargeScreen, setIsLargeScreen] = useState(false);

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
    <div className="flex gap-10 lg:flex-row flex-col-reverse w-full lg:max-w-[1000px] max-w-[620px]">
      <div className="flex basis-2/3 flex-col">
        <div className="table___title mb-4">Contact information</div>
        <Input className="h-12" placeholder="Name" />
        <Input className="h-12 mt-5" placeholder="Address" />
        <Input className="h-12 mt-5" placeholder="Phone Number" />
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
        <Button className="h-12 rounded-full mt-16">Checkout</Button>
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
                  {toVND(price ?? 0)} ({data.data.data.length} item
                  {data.data.data.length > 1 ? "s" : ""})
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
                    <span>{toVND(price ?? 0)}</span>
                  </div>
                  <div className="flex justify-between text-base mt-5">
                    <span>Estimated Delivery & Handling</span>
                    <span>Free</span>
                  </div>
                  <div className="flex justify-between text-base mt-5 py-4 border-t lg:border-b border-b-0 font-medium">
                    <span>Total</span>
                    <span>
                      <span>{toVND(price ?? 0)}</span>
                    </span>
                  </div>
                </div>
              </div>
            </AccordionContent>
          </AccordionItem>
        </Accordion>
      </div>
    </div>
  );
};

export default PaymentLayout;
