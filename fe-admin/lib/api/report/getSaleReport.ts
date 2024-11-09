import { SaleReportData, ReportFilterParam } from "@/lib/types";
import axios from "../axios";
import encodeParams from "@/lib/helpers/params";
import { AxiosResponse } from "axios";

export default function getSaleReport(
  params: ReportFilterParam
): Promise<AxiosResponse<SaleReportData>> {
  return axios.post<SaleReportData>(`/admin/saleReport?${encodeParams(params, false)}`);
}
