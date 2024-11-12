import localFont from "next/font/local";

export const Helvetica = localFont({
  src: [
    { path: "./HelveticaNeue.otf", weight: "400" },
    { path: "./HelveticaNeueBold.ttf", weight: "700" },
    { path: "./HelveticaNeueMedium.otf", weight: "500" },
    { path: "./HelveticaNeueLight.ttf", weight: "300" },
  ],
});
