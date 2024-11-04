import React from "react";
import { Button } from "../ui/button";
import { deleteStaff } from "@/lib/api/staff/deleteStaff";
import { toast } from "@/hooks/use-toast";
import { FaTrash } from "react-icons/fa";
import ConfirmDialog from "../ui/confirm-dialog";
import { AxiosError } from "axios";
import { ApiError } from "@/lib/types";

interface DeleteStaffProps {
  id: number;
  onDelete: () => void;
}

const DeleteStaff = ({ id, onDelete }: DeleteStaffProps) => {
  const handleDelete = () => {
    deleteStaff(id)
      .then(() => {
        onDelete();
        return toast({
          variant: "success",
          title: "Success",
          description: "Edit staff successfully",
        });
      })

      .catch((err: AxiosError<ApiError>) => {
        toast({
          variant: "destructive",
          title: "Error",
          description: err.response?.data.message ?? "Edit staff failed",
        });
      });
  };

  return (
    <ConfirmDialog
      title={"Confirmation"}
      description="Are you sure you want to delete this user?"
      handleYes={() => handleDelete()}
    >
      <Button
        size="icon"
        variant="outline"
        className="lg:px-4 px-2 whitespace-nowrap rounded-full text-fs-error hover:text-fs-error"
      >
        <div className="flex flex-wrap gap-1 items-center">
          <FaTrash />
        </div>
      </Button>
    </ConfirmDialog>
  );
};

export default DeleteStaff;
