"use client";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Button } from "../ui/button";
import { useSWRConfig } from "swr";
import Filter from "../filter/filter";
import { useNotificationList } from "@/hooks/useNotificationList";
import { notiFilterValues } from "@/lib/constants";
import { ApiError, FormFilterValues, Notification } from "@/lib/types";
import NotificationItem from "./notification-item";
import ViewMoreLink from "@/app/(private)/admin/notifications/view-more-link";
import { useAuth } from "../auth/auth-context";
import CreateNotification from "./create-notification";
import { seeAllNotification } from "@/lib/api/notification/seeAllNotification";
import { AxiosError } from "axios";
import { toast } from "@/hooks/use-toast";
import { UNSEEN_KEY } from "@/hooks/useUnseenNumber";
import NotiListSkeleton from "./noti-list-skeleton";

const NotificationList = () => {
  const { mutate: mutate2 } = useSWRConfig();
  const router = useRouter();
  const { user } = useAuth();

  const { filters, data, isLoading, error, mutate } = useNotificationList();
  const notificationsData = data?.data;

  const [openFilter, setOpenFilter] = useState(false);

  const onAllSeen = async () => {
    seeAllNotification()
      .then(() => {
        void mutate();
        mutate2(UNSEEN_KEY);
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description:
            err.response?.data.message ??
            "Mark as seen all notification failed",
        });
      });
  };

  const onApplyFilters = (data: FormFilterValues) => {
    const updatedParams = new URLSearchParams();
    data.filters.forEach(({ type, value }) => {
      if (value) {
        updatedParams.set(type, value);
      } else {
        updatedParams.delete(type);
      }
    });
    setOpenFilter(false);
    router.push(`?${updatedParams.toString()}`);
  };

  if (isLoading) {
    return <NotiListSkeleton number={3} />;
  } else if (error) {
    return <div>Failed to load</div>;
  }
  return (
    <div>
      <div className="pb-5 mb-7 border-b w-full flex flex-row justify-between items-center">
        <h1 className="table___title">Notifications</h1>
        {user?.address && <CreateNotification onCreated={() => mutate()} />}
      </div>
      <div className="w-full flex flex-col overflow-x-auto">
        <div className="mb-4 flex gap-3">
          <Filter
            title="Filter staffs"
            filters={filters}
            filterValues={notiFilterValues}
            open={openFilter}
            onOpenChange={(open) => {
              setOpenFilter(open);
            }}
            onApplyFilters={onApplyFilters}
          />
        </div>
        <Button
          className="font-normal text-sm ml-auto p-0"
          variant={"link"}
          type="button"
          onClick={() => onAllSeen()}
        >
          Seen all
        </Button>
        {notificationsData?.data && notificationsData?.data.length > 0 ? (
          notificationsData.data.map((item: Notification) => (
            <NotificationItem
              key={item.id}
              item={item}
              onUpdated={() => mutate()}
            />
          ))
        ) : (
          <div className="flex justify-center py-20">Nothing found.</div>
        )}
        {notificationsData?.page &&
        notificationsData?.page.limit <
          notificationsData?.page.totalElements ? (
          <div className="flex items-center justify-end space-x-2 py-4">
            <ViewMoreLink />
          </div>
        ) : null}
      </div>
    </div>
  );
};

export default NotificationList;
