package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.StockChangeHistory;
import com.fashion.backend.entity.StockReport;
import com.fashion.backend.entity.StockReportDetail;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.stockreport.FindStockReportRequest;
import com.fashion.backend.payload.stockreport.StockReportDetailResponse;
import com.fashion.backend.payload.stockreport.StockReportResponse;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.StockChangeHistoryRepository;
import com.fashion.backend.repository.StockReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockReportService {
	private final StockChangeHistoryRepository stockChangeHistoryRepository;
	private final StockReportRepository stockReportRepository;
	private final ItemRepository itemRepository;

	private boolean validateDate(int timeFrom, int timeTo) {
		Date timeFromDate = new Date(timeFrom * 1000L);
		Date timeToDate = new Date(timeTo * 1000L);

		return timeFromDate.after(timeToDate);
	}

	public StockReportResponse findStockReport(FindStockReportRequest request) {
		if (validateDate(request.getTimeFrom(), request.getTimeTo())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.TIME_FROM_TIME_TO_VALIDATE);
		}

		Date timeFrom = new java.sql.Date((request.getTimeFrom() - 24 * 60 * 60) * 1000L);
		Date timeTo = new java.sql.Date((request.getTimeTo() + 24 * 60 * 60) * 1000L );

		Date now = new Date();
		if (now.before(timeFrom)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.StockReport.FUTURE_DATE_INVALID);
		}

		Optional<StockReport> checkedReport = stockReportRepository.findFirstByTimeFromAndTimeTo(timeFrom, timeTo);
		if (checkedReport.isPresent()) {
			return mapToDTO(checkedReport.get());
		}

		boolean creatingNew = timeTo.before(now);

		int qtyInit = 0;
		int qtySell = 0;
		int qtyPayback = 0;
		int qtyIncrease = 0;
		int qtyDecrease = 0;
		int qtyFinal = 0;

		List<StockReportDetail> reportDetails	 = new ArrayList<>();
		List<Item> allItems = itemRepository.findAll();
		for (Item item : allItems) {
			List<StockChangeHistory> stockChanges =
					stockChangeHistoryRepository.findAllByItemIdAndCreatedAtAfterAndCreatedAtBefore(
							item.getId(),
							timeFrom,
							timeTo);

			int sellAmount = 0, increaseAmount = 0, decreaseAmount = 0, paybackAmount = 0;
			for (StockChangeHistory change : stockChanges) {
				int quantity = change.getQuantity();
				switch (change.getType()) {
					case SELL -> sellAmount += quantity;
					case INCREASE -> increaseAmount += quantity;
					case DECREASE -> decreaseAmount += quantity;
					case PAYBACK -> paybackAmount += quantity;
				}
			}

			PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("createdAt")));
			List<StockChangeHistory> nearlies = stockChangeHistoryRepository.findByItemIdAndCreatedAtLessThanEqual(item.getId(), timeFrom, pageRequest);
			StockChangeHistory nearlyOptional = nearlies.isEmpty() ? null : nearlies.get(0);

			int initial = nearlyOptional == null ? 0 : nearlyOptional.getQuantityLeft();

			int finalAmount = (stockChanges.isEmpty()) ?
							  initial :
							  stockChanges.get(stockChanges.size() - 1).getQuantityLeft();
			if (initial == 0) {
				initial = finalAmount - increaseAmount - sellAmount - decreaseAmount - paybackAmount;
			}

			if (initial != 0 || sellAmount != 0 || increaseAmount != 0 || decreaseAmount != 0 || paybackAmount != 0) {
				StockReportDetail detail = StockReportDetail.builder()
															.item(item)
															.initial(initial)
															.sell(sellAmount)
															.increase(increaseAmount)
															.decrease(decreaseAmount)
															.payback(paybackAmount)
															.finalQty(finalAmount)
															.build();
				reportDetails.add(detail);
			}

			qtyInit += initial;
			qtyIncrease += increaseAmount;
			qtyDecrease += decreaseAmount;
			qtySell += sellAmount;
			qtyPayback += paybackAmount;
			qtyFinal += finalAmount;
		}

		StockReport stockReport = StockReport.builder()
											 .details(reportDetails)
											 .timeFrom(timeFrom)
											 .timeTo(timeTo)
											 .initial(qtyInit)
											 .increase(qtyIncrease)
											 .decrease(qtyDecrease)
											 .sell(qtySell)
											 .payback(qtyPayback)
											 .finalQty(qtyFinal)
											 .build();

		if (creatingNew) {
			stockReport = stockReportRepository.save(stockReport);
		}

		return mapToDTO(stockReport);
	}

	private StockReportResponse mapToDTO(StockReport report) {
		return StockReportResponse.builder()
								  .timeFrom(report.getTimeFrom())
								  .timeTo(report.getTimeTo())
								  .sell(report.getSell())
								  .initial(report.getInitial())
								  .increase(report.getIncrease())
								  .decrease(report.getDecrease())
								  .payback(report.getPayback())
								  .finalQty(report.getFinalQty())
								  .details(report.getDetails().stream().map(this::mapToDTO).toList())
								  .build();
	}

	private StockReportDetailResponse mapToDTO(StockReportDetail detail) {
		return StockReportDetailResponse.builder()
										.item(mapToDTO(detail.getItem()))
										.sell(detail.getSell())
										.initial(detail.getInitial())
										.increase(detail.getIncrease())
										.decrease(detail.getDecrease())
										.payback(detail.getPayback())
										.finalQty(detail.getFinalQty())
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
