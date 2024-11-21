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

import { Category } from "@/lib/types";
import CreateCategoryDialog from "./create-category";
import EditCategoryDialog from "./edit-category";
import DeleteCategory from "./delete-category";
import TableSkeleton from "../table-skeleton";
import { useCategoryList } from "@/hooks/useCategory";

export const columns: ColumnDef<Category>[] = [
  {
    accessorKey: "id",
    header: () => {
      return <span className="font-semibold">ID</span>;
    },
    cell: ({ row }) => <div>{row.getValue("id")}</div>,
  },
  {
    accessorKey: "name",
    header: () => {
      return <span className="font-semibold">Name</span>;
    },
    cell: ({ row }) => <div className="capitalize">{row.getValue("name")}</div>,
  },
  {
    accessorKey: "actions",
    header: () => {
      return <div className="font-semibold">Actions</div>;
    },
    cell: () => <></>,
  },
];

export function CategoryTable() {
  const { data, isLoading, error, mutate } = useCategoryList();

  const categories: Category[] = data?.data.data || [];
  const table = useReactTable({
    data: categories,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

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
          <h1 className="table___title">Manage Category</h1>
          <CreateCategoryDialog onAdded={() => void mutate()} />
        </div>

        <div className="rounded-md border overflow-x-auto min-w-full max-w-[50vw] mt-4">
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
                        <TableCell key={cell.id} className="flex py-2 gap-2">
                          <EditCategoryDialog
                            category={row.original}
                            onAdded={() => void mutate()}
                          />
                          <DeleteCategory
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
      </div>
    );
}
