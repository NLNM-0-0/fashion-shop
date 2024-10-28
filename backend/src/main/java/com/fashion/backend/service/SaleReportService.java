package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.Order;
import com.fashion.backend.entity.OrderDetail;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.salereport.FindSaleReportRequest;
import com.fashion.backend.payload.salereport.SaleReportDetailResponse;
import com.fashion.backend.payload.salereport.SaleReportResponse;
import com.fashion.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleReportService {
	private final OrderRepository orderRepository;


	private boolean validateDate(int timeFrom, int timeTo) {
		Date timeFromDate = new Date(timeFrom * 1000L);
		Date timeToDate = new Date(timeTo * 1000L);

		return timeFromDate.after(timeToDate);
	}

	public SaleReportResponse findSaleReport(FindSaleReportRequest request) {
		if (validateDate(request.getTimeFrom(), request.getTimeTo())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.TIME_FROM_TIME_TO_VALIDATE);
		}

		// Convert timestamps to Date
		Date timeFrom = new Date(request.getTimeFrom() * 1000L);
		Date timeTo = new Date(request.getTimeTo() * 1000L);

		// Fetch all invoices within the specified date range
		List<Order> allOrders = orderRepository.findAllByTimeFromAndTimeTo(timeFrom, timeTo);
		if (allOrders.isEmpty()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.SaleReport.NO_ORDER_BETWEEN_TIME);
		}

		// Initialize tracking variables
		int total = 0;
		int totalAmount = 0;
		Map<Long, Integer> mapAmount = new HashMap<>();
		Map<Long, Item> mapItem = new HashMap<>();
		Map<Long, Integer> mapSales = new HashMap<>();

		for (Order order : allOrders) {
			// Fetch details for each invoice
			List<OrderDetail> details = order.getOrderDetails();

			for (OrderDetail detail : details) {
				// Process item sales data
				Long itemId = detail.getId();
				mapAmount.merge(itemId, detail.getQuantity(), Integer::sum);
				mapItem.put(itemId, detail.getItem());

				int totalInvoiceDetail = detail.getUnitPrice() * detail.getQuantity();
				mapSales.merge(itemId, totalInvoiceDetail, Integer::sum);

				total += totalInvoiceDetail;
				totalAmount += detail.getQuantity();
			}
		}

		// Prepare report details
		List<SaleReportDetailResponse> reportDetails = new ArrayList<>();
		for (Long itemId : mapItem.keySet()) {
			if (mapAmount.getOrDefault(itemId, 0) > 0) {
				SaleReportDetailResponse detail = SaleReportDetailResponse.builder()
																		  .item(mapToDTO(mapItem.get(itemId)))
																		  .amount(mapAmount.get(itemId))
																		  .totalSales(mapSales.get(itemId))
																		  .build();
				reportDetails.add(detail);
			}
		}

		// Assemble and return the sales report
		return SaleReportResponse.builder()
								 .timeFrom(timeFrom)
								 .timeTo(timeTo)
								 .total(total)
								 .amount(totalAmount)
								 .details(reportDetails)
								 .build();
	}

	private SimpleItemResponse mapToDTO(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .image(item.getImage())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
