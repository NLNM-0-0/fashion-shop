import { useEffect, useRef, useState } from "react";

const OTP_EXPIRED_TIME = 60;

export default function useCountDown() {
  const [wasOptSend, setWasOptSend] = useState(false);
  const [remainingTime, setRemainingTime] = useState(0);

  const intervalIdRef = useRef<NodeJS.Timer | undefined>(undefined);

  useEffect(() => {
    let intervalID: NodeJS.Timeout;
    if (wasOptSend) {
      setRemainingTime(OTP_EXPIRED_TIME);
      intervalID = setInterval(() => {
        setRemainingTime((t) => {
          if (t > 0) {
            return t - 1;
          }
          setWasOptSend(false);
          return t;
        });
      }, 1000);
      intervalIdRef.current = intervalID;
    } else {
      setRemainingTime(0);
    }
    return () => clearInterval(intervalID);
  }, [wasOptSend]);

  return {
    remainingTime,
    wasOptSend,
    startCountdown: () => setWasOptSend(true),
    stopCountdown: () => setWasOptSend(false),
  };
}
