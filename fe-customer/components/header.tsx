"use client";

import CartButton from "./cart/cart-icon";
import CategoryHeader from "./category/category-header";
import FavoriteButton from "./favorite/favorite-button";
import LogoButton from "./logo-button";
import Profile from "./profile/profile";

const Header = () => {
  return (
    <div className="flex z-10 relative py-3 w-[100%] px-4 bg-white">
      <div className="flex flex-1 items-center justify-between sm:flex-row flex-col-reverse gap-2">
        <div className="sm:block hidden">
          <LogoButton />
        </div>
        <CategoryHeader />
        <div className="flex justify-between items-center gap-2 pl-2 sm:w-auto w-full">
          <div className="sm:hidden block">
            <LogoButton />
          </div>
          <div className="flex gap-2">
            <CartButton />
            <FavoriteButton />
            <Profile />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Header;
