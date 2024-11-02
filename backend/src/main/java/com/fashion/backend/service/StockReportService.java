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

		Date timeFrom = new Date(request.getTimeFrom() * 1000L);
		Date timeTo = new Date(request.getTimeTo() * 1000L);

		Optional<StockReport> reportOptional = stockReportRepository.findFirstByTimeFromAndTimeTo(timeFrom, timeTo);
		if (reportOptional.isPresent()) {
			return mapToDTO(reportOptional.get());
		}

		Date now = new Date();
		if (now.before(timeFrom)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.StockReport.FUTURE_DATE_INVALID);
		}
		boolean creatingNew = timeTo.before(now);

		int qtyInit = 0;
		int qtySell = 0;
		int qtyPayback = 0;
		int qtyIncrease = 0;
		int qtyDecrease = 0;
		int qtyFinal = 0;

		List<StockReportDetail> allDetails = new ArrayList<>();
		List<Item> allItems = itemRepository.findAll();
		for (Item item : allItems) {
			List<StockChangeHistory> stockChanges =
					stockChangeHistoryRepository.findAllStockChangesForReport(
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

			Optional<StockChangeHistory> nearlyOptional =
					stockChangeHistoryRepository.getNearlyStockChangeHistory(item.getId(), timeFrom);
			int initial = nearlyOptional.map(StockChangeHistory::getQuantityLeft).orElse(0);

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
				allDetails.add(detail);
			}

			qtyInit += initial;
			qtyIncrease += increaseAmount;
			qtyDecrease += decreaseAmount;
			qtySell += sellAmount;
			qtyPayback += paybackAmount;
			qtyFinal += finalAmount;
		}

		StockReport stockReport = StockReport.builder()
											 .details(allDetails)
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
								 .image(item.getImage())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
