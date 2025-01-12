package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.Order;
import com.fashion.backend.entity.OrderDetail;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.salereport.FindSaleReportRequest;
import com.fashion.backend.payload.salereport.SaleReportDetailResponse;
import com.fashion.backend.payload.salereport.SaleReportResponse;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleReportService {
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;


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
		Date timeFrom = new Date((request.getTimeFrom() - 24 * 60 * 60) * 1000L);
		Date timeTo = new Date((request.getTimeTo() + 24 * 60 * 60) * 1000L );

		// Fetch all invoices within the specified date range
		List<Order> allOrders = orderRepository.findAllByStatusAndUpdatedAtBetween(OrderStatus.DONE, timeFrom, timeTo);
		if (allOrders.isEmpty()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.SaleReport.NO_ORDER_BETWEEN_TIME);
		}

		// Initialize tracking variables
		int totalSales = 0;
		int totalAmount = 0;
		Map<Long, Integer> mapAmount = new HashMap<>();
		Map<Long, Integer> mapSales = new HashMap<>();

		for (Order order : allOrders) {
			// Fetch details for each invoice
			List<OrderDetail> details = order.getOrderDetails();

			for (OrderDetail detail : details) {
				// Process item sales data
				Long itemId = detail.getId();
				mapAmount.merge(itemId, detail.getQuantity(), Integer::sum);

				int totalInvoiceDetail = detail.getUnitPrice() * detail.getQuantity();
				mapSales.merge(itemId, totalInvoiceDetail, Integer::sum);

				totalSales += totalInvoiceDetail;
				totalAmount += detail.getQuantity();
			}
		}

		// Prepare report details
		List<SaleReportDetailResponse> reportDetails = mapAmount.entrySet().stream()
																.filter(entry -> entry.getValue() > 0) // Include only items with quantity > 0
																.map(entry -> {
																	Long itemId = entry.getKey();
																	return SaleReportDetailResponse.builder()
																								   .item(mapToDTO(itemRepository.findById(itemId).get())) // Fetch item DTO directly
																								   .amount(entry.getValue())
																								   .totalSales(mapSales.getOrDefault(itemId, 0))
																								   .build();
																})
																.collect(Collectors.toList());

		// Assemble and return the sales report
		return SaleReportResponse.builder()
								 .timeFrom(timeFrom)
								 .timeTo(timeTo)
								 .total(totalSales)
								 .amount(totalAmount)
								 .details(reportDetails)
								 .build();
	}

	private SimpleItemResponse mapToDTO(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .unitPrice(item.getUnitPrice())
								 .images(item.getImages())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
