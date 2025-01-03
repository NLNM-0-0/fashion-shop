"use client";

import * as React from "react";
import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";

import { Button, buttonVariants } from "@/components/ui/button";

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
import { FormFilterValues, Product } from "@/lib/types";
import { productFilterValues } from "@/lib/constants";
import Filter from "../filter/filter";
import TableSkeleton from "../table-skeleton";
import { useProductList } from "@/hooks/useProductList";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { FaPen, FaPlus } from "react-icons/fa";
import Link from "next/link";
import { cn, toVND } from "@/lib/utils";
import DeleteProduct from "./delete-product";

export const columns: ColumnDef<Product>[] = [
  {
    accessorKey: "id",
    header: () => {
      return <span className="font-semibold">ID</span>;
    },
    cell: ({ row }) => <div>{row.getValue("id")}</div>,
  },
  {
    accessorKey: "image",
    header: () => {},
    cell: ({ row }) => (
      <div className="flex justify-end">
        <Avatar>
          <AvatarImage src={row.original.images[0] ?? ""} alt="img" />
          <AvatarFallback>{row.original.name.substring(0, 2)}</AvatarFallback>
        </Avatar>
      </div>
    ),
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
    accessorKey: "unitPrice",
    header: () => (
      <div className="flex justify-end">
        <span className="font-semibold">Price</span>
      </div>
    ),
    cell: ({ row }) => {
      const unitPrice = parseFloat(row.getValue("unitPrice"));

      return <div className="text-right font-medium">{toVND(unitPrice)}</div>;
    },
  },
  {
    accessorKey: "actions",
    header: () => {
      return <div className="font-semibold">Actions</div>;
    },
    cell: () => <></>,
  },
];

export function ProductTable() {
  const router = useRouter();
  const { filters, data, isLoading, error, mutate } = useProductList();

  const customers: Product[] = data?.data.data || [];
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

  if (isLoading || !data) {
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
        <div className="flex justify-between items-center">
          <h1 className="table___title">Manage Product</h1>
          <Link
            href={"/admin/products/new"}
            className={cn(
              "px-4 whitespace-nowrap",
              buttonVariants({ variant: "default" })
            )}
          >
            <div className="flex flex-wrap gap-1 items-center capitalize">
              <FaPlus />
              Add new product
            </div>
          </Link>
        </div>
        <Filter
          title="Filter products"
          filters={filters}
          filterValues={productFilterValues}
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
                    {row.getVisibleCells().map((cell) =>
                      cell.id.includes("actions") ? (
                        <TableCell key={cell.id} className="flex gap-2">
                          <Link
                            href={`/admin/products/${row.original.id}`}
                            className={cn(
                              buttonVariants({
                                variant: "outline",
                                size: "icon",
                              }),
                              "whitespace-nowrap rounded-full text-gray-500 hover:text-gray-500"
                            )}
                          >
                            <FaPen />
                          </Link>
                          <DeleteProduct
                            id={row.original.id}
                            onDelete={() => void mutate()}
                          />
                        </TableCell>
                      ) : (
                        <TableCell key={cell.id}>
                          {flexRender(
                            cell.column.columnDef.cell,
                            cell.getContext()
                          )}
                        </TableCell>
                      )
                    )}
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
