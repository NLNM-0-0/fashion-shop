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
