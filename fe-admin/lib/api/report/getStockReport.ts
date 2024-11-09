import { StockReportData, ReportFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getStockReport(
  params: ReportFilterParam
): Promise<AxiosResponse<StockReportData>> {
  return axios.post<StockReportData>(
    `/admin/stockReport?${encodeParams(params, false)}`
  );
}
