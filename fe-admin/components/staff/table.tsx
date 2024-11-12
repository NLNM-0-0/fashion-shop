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
import { useState } from "react";

import { useRouter } from "next/navigation";

import Paging from "../paging";
import { FormFilterValues, Staff } from "@/lib/types";
import { staffFilterValues } from "@/lib/constants";
import { useStaffList } from "@/hooks/useStaffList";
import Filter from "../filter/filter";
import CreateStaffDialog from "./create-staff";
import EditStaffDialog from "./edit-staff";
import DeleteStaff from "./delete-staff";
import TableSkeleton from "../table-skeleton";

export const columns: ColumnDef<Staff>[] = [
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
    accessorKey: "actions",
    header: () => {
      return <div className="font-semibold">Actions</div>;
    },
    cell: () => <></>,
  },
];

export function StaffTable() {
  const router = useRouter();
  const { filters, data, isLoading, error, mutate } = useStaffList();

  const staffs: Staff[] = data?.data.data || [];
  const table = useReactTable({
    data: staffs,
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
          <h1 className="table___title">Manage Staff</h1>
          <CreateStaffDialog onAdded={() => void mutate()} />
        </div>
        <Filter
          title="Filter staffs"
          filters={filters}
          filterValues={staffFilterValues}
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
                        <div key={cell.id} className="flex py-2 gap-2">
                          <EditStaffDialog
                            staff={row.original}
                            onAdded={() => void mutate()}
                          />
                          <DeleteStaff
                            id={row.original.id}
                            onDelete={() => void mutate()}
                          />
                        </div>
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
