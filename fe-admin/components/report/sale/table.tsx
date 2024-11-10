"use client";

import * as React from "react";
import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { useEffect } from "react";

import { SaleReportItem } from "@/lib/types";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useSaleReportList } from "@/hooks/useSaleReportList";
import TableSkeleton from "@/components/table-skeleton";
import ReportFilter, {
  ReportFilterValue,
} from "@/components/filter/report-filter";
import { FilterParams } from "@/hooks/useFilterList";
import { unknown } from "zod";

export const columns: ColumnDef<SaleReportItem>[] = [
  {
    accessorKey: "id",
    header: () => {
      return <span className="font-semibold">ProdID</span>;
    },
    cell: ({ row }) => <div>{row.original.item.id}</div>,
  },
  {
    accessorKey: "image",
    header: () => {},
    cell: ({ row }) => (
      <div className="flex justify-end">
        <Avatar>
          <AvatarImage src={row.getValue("image")} alt="img" />
          <AvatarFallback>
            {row.original.item.name.substring(0, 2)}
          </AvatarFallback>
        </Avatar>
      </div>
    ),
  },
  {
    accessorKey: "name",
    header: () => {
      return <span className="font-semibold">Name</span>;
    },
    cell: ({ row }) => (
      <div className="capitalize">{row.original.item.name}</div>
    ),
  },
  {
    accessorKey: "amount",
    header: () => (
      <div className="flex justify-end">
        <span className="font-semibold">Quantity</span>
      </div>
    ),
    cell: ({ row }) => {
      return (
        <div className="text-right font-medium">{row.original.amount}</div>
      );
    },
  },
  {
    accessorKey: "totalSales",
    header: () => (
      <div className="flex justify-end">
        <span className="font-semibold">Total Sales</span>
      </div>
    ),
    cell: ({ row }) => {
      const totalSales = parseFloat(row.getValue("totalSales"));

      return <div className="text-right font-medium">{totalSales}</div>;
    },
  },
];
export function SaleReportTable() {
  const { filters, data, isLoading, error, updateFilters } =
    useSaleReportList();

  const customers: SaleReportItem[] = data?.data.details || [];
  const table = useReactTable({
    data: customers,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  const onApplyFilters = (data: ReportFilterValue) => {
    const filterParams: FilterParams = {
      ...data,
    };
    updateFilters(filterParams);
  };
  useEffect(() => {
    const now = new Date().setHours(0, 0, 0, 0);
    const seconds = Math.round(now.valueOf() / 1000);
    const initialFilters: FilterParams = {
      timeTo: seconds.toString(),
      timeFrom: (seconds - 2592000).toString(),
    };
    updateFilters(initialFilters);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (isLoading) {
    return (
      <TableSkeleton
        isHasExtensionAction={false}
        isHasFilter={true}
        isHasSearch={true}
        isHasChooseVisibleRow={false}
        isHasCheckBox={false}
        isHasPaging={true}
        numberRow={5}
        cells={[
          {
            percent: 1,
          },
          {
            percent: 5,
          },
          {
            percent: 1,
          },
        ]}
      ></TableSkeleton>
    );
  } else if (error) {
    return (
      <>
        <div>Failed to load</div>
      </>
    );
  } else
    return (
      <div className="w-full">
        <div className="flex justify-between items-center">
          <h1 className="table___title">Sale Report</h1>
        </div>
        <ReportFilter filters={filters} onApplyFilters={onApplyFilters} />
        <div className="rounded-md border overflow-x-auto min-w-full max-w-[50vw]">
          <Table className="min-w-full w-max">
            <TableHeader>
              {table.getHeaderGroups().map((headerGroup) => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map((header) => {
                    return (
                      <TableHead key={header.id}>
                        {header.isPlaceholder
                          ? null
                          : flexRender(
                              header.column.columnDef.header,
                              header.getContext()
                            )}
                      </TableHead>
                    );
                  })}
                </TableRow>
              ))}
              <TableRow key={"subHeaderRow"}>
                <TableHead key={"header-final"} className="col-span-2">
                  {flexRender(
                    <div className="font-semibold">Total:</div>,
                    unknown
                  )}
                </TableHead>
                <TableHead key={"header-empty-1"}></TableHead>
                <TableHead key={"header-empty-2"}></TableHead>
                <TableHead key={"header-amount"}>
                  {flexRender(
                    <div className="text-right font-semibold">
                      {data?.data.amount}
                    </div>,
                    unknown
                  )}
                </TableHead>
                <TableHead key={"header-money"}>
                  {flexRender(
                    <div className="text-right font-semibold">
                      {data?.data.total}
                    </div>,
                    unknown
                  )}
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {table.getRowModel().rows?.length ? (
                table.getRowModel().rows.map((row) => (
                  <TableRow
                    key={row.id}
                    data-state={row.getIsSelected() && "selected"}
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext()
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-24 text-center"
                  >
                    Nothing found.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>
      </div>
    );
}
