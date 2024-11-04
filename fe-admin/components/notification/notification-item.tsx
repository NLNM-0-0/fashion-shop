import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { LuCheck } from "react-icons/lu";
import { Notification } from "@/lib/types";
import { useSWRConfig } from "swr";
import { seeNotification } from "@/lib/api/notification/seeNotification";
import { UNSEEN_KEY } from "@/hooks/useUnseenNumber";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";
import { toast } from "@/hooks/use-toast";
import { dateTimeStringFormat } from "@/lib/helpers/date";

const NotificationItem = ({
  item,
  onUpdated,
}: {
  item: Notification;
  onUpdated: () => void;
}) => {
  const { mutate } = useSWRConfig();
  const onSumit = async () => {
    seeNotification(item.id)
      .then(() => {
        if (onUpdated) {
          onUpdated();
        }
        mutate(UNSEEN_KEY);
      })
      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description:
            err.response?.data.message ?? "Mark notification as seen failed",
        });
      });
  };

  return (
    <div
      className={`py-4 my-2 border-b border-b-border flex flex-row gap-3 pl-2 border-l-4 ${
        item.seen ? "border-white" : "border-primary"
      }`}
    >
      <Avatar>
        <AvatarImage src={item.from?.image ?? ""} alt="avatar" />
        <AvatarFallback>{item.from?.name.substring(0, 2)}</AvatarFallback>
      </Avatar>
      <div className="flex flex-col gap-2 flex-1">
        <div className="flex text-sm items-center">
          <h2 className="font-medium text-lg">{item.from?.name}</h2>
          <h2 className="font-medium text-gray-text ml-auto text-sm">
            {dateTimeStringFormat(item.createdAt)}
          </h2>
          {item.seen ? null : (
            <div
              className="hover:text-primary text-gray-text transition-colors cursor-pointer p-1"
              title="Mark as seen"
              onClick={() => onSumit()}
            >
              <LuCheck className="h-5 w-5" />
            </div>
          )}
        </div>
        <span className="font-medium">{item.title} </span>
        <div>
          <span className="font-light ">{item.description}</span>
        </div>
      </div>
    </div>
  );
};

export default NotificationItem;
