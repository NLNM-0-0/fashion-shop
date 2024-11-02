"use client";

import * as React from "react";
import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";

import { Button } from "@/components/ui/button";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { useState } from "react";

import { useRouter } from "next/navigation";

import Paging from "../paging";
import { Customer, FormFilterValues } from "@/lib/types";
import { useCustomerList } from "@/hooks/useCustomerList";
import { customerFilterValues } from "@/lib/constants";
import Filter from "../filter/filter";
import TableSkeleton from "../table-skeleton";

export const columns: ColumnDef<Customer>[] = [
  {
    accessorKey: "id",
    header: () => {
      return <span className="font-semibold">ID</span>;
    },
    cell: ({ row }) => <div>{row.getValue("id")}</div>,
  },
  {
    accessorKey: "name",
    header: ({ column }) => {
      return (
        <Button
          className="p-2"
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          <span className="font-semibold">Name</span>
        </Button>
      );
    },
    cell: ({ row }) => <div className="capitalize">{row.getValue("name")}</div>,
  },
  {
    accessorKey: "email",
    header: () => {
      return <div className="font-semibold">Email</div>;
    },
    cell: ({ row }) => (
      <div className="lg:max-w-[16rem] max-w-[3rem] truncate">
        {row.getValue("email")}
      </div>
    ),
  },
  {
    accessorKey: "phone",
    header: () => {
      return <div className="font-semibold flex justify-end">Phone</div>;
    },
    cell: ({ row }) => (
      <div className="text-right">{row.getValue("phone")}</div>
    ),
  },
];

export function CustomerTable() {
  const router = useRouter();
  const { filters, data, isLoading, error } = useCustomerList();

  const customers: Customer[] = data?.data.data || [];
  const table = useReactTable({
    data: customers,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });
  const [openFilter, setOpenFilter] = useState(false);

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
    return <div>Failed to load</div>;
  } else
    return (
      <div className="w-full">
        <Filter
          title="Filter customers"
          filters={filters}
          filterValues={customerFilterValues}
          open={openFilter}
          onOpenChange={(open) => {
            setOpenFilter(open);
          }}
          onApplyFilters={onApplyFilters}
        />
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
        <div className="flex items-center justify-end space-x-2 py-4">
          <Paging
            page={data?.data.page.index.toString() ?? "1"}
            totalPage={data?.data.page.totalPages ?? 1}
          />
        </div>
      </div>
    );
}
