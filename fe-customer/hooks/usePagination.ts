import { useRouter, useSearchParams } from "next/navigation";

interface UsePaginatedFiltersProps {
  totalPage: number;
}

export const usePagination = ({ totalPage }: UsePaginatedFiltersProps) => {
  const router = useRouter();
  const searchParams = useSearchParams();

  const goToNextPage = () => {
    const currentPage = parseInt(searchParams.get("page") || "1", 10);
    const newPage = Math.min(totalPage, currentPage + 1);
    updatePageParam(newPage);
  };

  const goBack = () => {
    const currentPage = parseInt(searchParams.get("page") || "1", 10);
    const newPage = Math.max(1, currentPage - 1);
    updatePageParam(newPage);
  };

  const goToPage = (page: number) => {
    const newPage = Math.max(1, Math.min(totalPage, page));
    updatePageParam(newPage);
  };

  const updatePageParam = (page: number) => {
    const updatedParams = new URLSearchParams(searchParams.toString());
    updatedParams.set("page", page.toString());
    router.push(`?${updatedParams.toString()}`);
  };

  return { goToNextPage, goBack, goToPage };
};
