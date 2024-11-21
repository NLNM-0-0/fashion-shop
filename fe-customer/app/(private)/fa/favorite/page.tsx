import FavoriteList from "@/components/favorite/favorite-list";
import { Metadata } from "next";
export const metadata: Metadata = {
  title: "My Favorite",
};
const FavoritePage = () => {
  return (
    <>
      <FavoriteList />
    </>
  );
};

export default FavoritePage;
