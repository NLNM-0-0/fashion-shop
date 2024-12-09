export enum FilterInputType {
  TEXT = "TEXT",
  NUMBER = "NUMBER",
  BOOLEAN = "BOOLEAN",
  YEAR = "YEAR",
  MONTH = "MONTH",
  // Memo: date in milliseconds
  DATE = "DATE",
}

export enum OrderStatus {
  PENDING = "PENDING",
  CONFIRMED = "CONFIRMED",
  SHIPPING = "SHIPPING",
  DONE = "DONE",
  CANCELED = "CANCELED",
}

export enum Gender {
  BOYS = "KIDS",
  MEN = "MEN",
  WOMEN = "WOMEN",
  UNISEX = "UNISEX",
}

export enum Color {
  BLACK = "BLACK",
  BLUE = "BLUE",
  BROWN = "BROWN",
  GREEN = "GREEN",
  GREY = "GREY",
  ORANGE = "ORANGE",
  PINK = "PINK",
  PURPLE = "PURPLE",
  RED = "RED",
  WHITE = "WHITE",
  YELLOW = "YELLOW",
  MULTI_COLOR = "MULTI_COLOR",
}

export enum Season {
  SPRING = "SPRING",
  SUMMER = "SUMMER",
  FALL = "FALL",
  WINTER = "WINTER",
}

export enum Price {
  ALL = "ALL",
  BELOW199 = "BELOW199",
  FROM199TO299 = "FROM199TO299",
  FROM299TO399 = "FROM299TO399",
  FROM399TO499 = "FROM399TO499",
  FROM499TO799 = "FROM499TO799",
  FROM799TO999 = "FROM799TO999",
  ABOVE999 = "ABOVE999",
}

export enum SortType {
  Newest = "Newest",
  Oldest = "Oldest",
  PriceHighLow = "Price: High-Low",
  PriceLowHigh = "Price: Low-High",
}
