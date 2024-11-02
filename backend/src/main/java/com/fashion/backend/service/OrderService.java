package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.constant.StockChangeType;
import com.fashion.backend.entity.*;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.mail.MailSender;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.customer.SimpleCustomerResponse;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.order.*;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.payload.staff.SimpleStaffResponse;
import com.fashion.backend.repository.*;
import com.fashion.backend.utils.tuple.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;
	private final StockChangeHistoryRepository stockChangeHistoryRepository;
	private final MailSender mailSender;
	private final NotificationRepository notificationRepository;

	@Transactional
	public ListResponse<SimpleOrderResponse, StaffOrderFilter> getOrders(AppPageRequest page, StaffOrderFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.DESC, "updatedAt"));

		Specification<Order> spec = filterOrders(filter);

		Page<Order> orderPage = orderRepository.findAll(spec, pageable);

		List<Order> orders = orderPage.getContent();

		List<SimpleOrderResponse> data = orders.stream().map(this::mapToSimpleDTO).toList();

		return ListResponse.<SimpleOrderResponse, StaffOrderFilter>builder()
						   .data(data)
						   .appPageResponse(AppPageResponse.builder()
														   .index(page.getPage())
														   .limit(page.getLimit())
														   .totalPages(orderPage.getTotalPages())
														   .totalElements(orderPage.getTotalElements())
														   .build())
						   .filter(filter)
						   .build();
	}

	@Transactional
	public ListResponse<SimpleOrderResponse, UserOrderFilter> getOrders(AppPageRequest page, UserOrderFilter filter) {
		User customer = Common.findCurrUser(userRepository, userAuthRepository);

		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.DESC, "updatedAt"));

		Specification<Order> spec = filterOrders(customer.getId(), filter);

		Page<Order> orderPage = orderRepository.findAll(spec, pageable);

		List<Order> orders = orderPage.getContent();

		List<SimpleOrderResponse> data = orders.stream().map(this::mapToSimpleDTO).toList();

		return ListResponse.<SimpleOrderResponse, UserOrderFilter>builder()
						   .data(data)
						   .appPageResponse(AppPageResponse.builder()
														   .index(page.getPage())
														   .limit(page.getLimit())
														   .totalPages(orderPage.getTotalPages())
														   .totalElements(orderPage.getTotalElements())
														   .build())
						   .filter(filter)
						   .build();
	}

	@Transactional
	public OrderResponse getOrder(Long orderId) {
		return mapToDTO(Common.findOrderById(orderId, orderRepository));
	}

	private Specification<Order> filterOrders(Long userId, UserOrderFilter filter) {
		Specification<Order> spec = OrderSpecs.hasCustomerId(userId);
		if (filter.getOrderStatus() != null) {
			spec = spec.and(OrderSpecs.hasStatus(filter.getOrderStatus().name()));
		}
		return spec;
	}

	private Specification<Order> filterOrders(StaffOrderFilter filter) {
		Specification<Order> spec = Specification.where(null);
		if (filter.getOrderStatus() != null) {
			spec = spec.and(OrderSpecs.hasStatus(filter.getOrderStatus().name()));
		}
		if (filter.getStaffName() != null) {
			spec = spec.and(OrderSpecs.hasStaffName(filter.getStaffName()));
		}
		if (filter.getCustomerName() != null) {
			spec = spec.and(OrderSpecs.hasCustomerName(filter.getCustomerName()));
		}
		return spec;
	}

	@Transactional
	public SimpleResponse receiveOrder(Long orderId) {
		Order order = Common.findOrderById(orderId, orderRepository);
		if (order.getStatus() == OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELED) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.CAN_NOT_BE_REACHED_CLOSED_ORDER);
		}

		User user = Common.findCurrUser(userRepository, userAuthRepository);
		boolean isMadeByCurrUser = Objects.equals(user.getId(), order.getCustomer().getId());
		if (!isMadeByCurrUser) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.ORDER_JUST_CAN_BE_REACHED_BY_OWNER_CUSTOMER);
		}

		order.setStatus(OrderStatus.DONE);

		orderRepository.save(order);

		sendEmailOrderStatusChange(order, order.getCustomer(), OrderStatus.DONE);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse changeOrderStatus(Long orderId, ChangeOrderStatus request) {
		Order order = Common.findOrderById(orderId, orderRepository);
		if (order.getStatus() == OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELED) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.CAN_NOT_BE_REACHED_CLOSED_ORDER);
		}

		OrderStatus orderStatus = request.getOrderStatus();
		order.setStatus(orderStatus);

		orderRepository.save(order);

		if (orderStatus == OrderStatus.CANCELED) {
			handlePaybackItem(order);
		}

		User user = Common.findCurrUser(userRepository, userAuthRepository);
		sendEmailOrderStatusChange(order, user, orderStatus);

		return new SimpleResponse();
	}

	private void sendEmailOrderStatusChange(Order order, User sender, OrderStatus orderStatus) {
		String title = "Order id " + order.getId() + " has changed status to " + orderStatus.name();
		String description
				= "Your order has changed status. Please make a check and inform us if something is not right.";

		Common.sendNotification(notificationRepository, mailSender, order.getCustomer(), sender, title, description);
	}

	@Transactional
	public OrderResponse placeOrder(PlaceOrderRequest request, boolean isMadeByStaff) {
		validateOrderDetails(request);

		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Pair<Integer, List<OrderDetail>> priceCalculation = calcPrice(request.getDetails());
		int totalPrice = priceCalculation.first();
		List<OrderDetail> details = priceCalculation.second();

		Order order = Order.builder()
						   .staff(isMadeByStaff ? user : null)
						   .customer(isMadeByStaff ? null : user)
						   .totalPrice(totalPrice)
						   .orderDetails(details)
						   .status(isMadeByStaff ? OrderStatus.DONE : OrderStatus.PENDING)
						   .build();

		order = orderRepository.save(order);

		handleOrderItem(order);

		return mapToDTO(order);
	}

	private void validateOrderDetails(PlaceOrderRequest request) {
		if (request.getDetails() == null || request.getDetails().isEmpty()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.ORDER_CAN_NOT_HAVE_NO_ITEM);
		}
		if (isContainOnlyUnique(request.getDetails().stream().map(OrderDetailRequest::getItemId).toList())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.ORDER_CAN_NOT_HAVE_SAME_ITEM);
		}
	}

	private boolean isContainOnlyUnique(List<Long> itemIds) {
		return new HashSet<>(itemIds).stream().toList().size() != itemIds.size();
	}

	private Pair<Integer, List<OrderDetail>> calcPrice(List<OrderDetailRequest> requestDetails) {
		int totalPrice = 0;
		List<OrderDetail> details = new ArrayList<>();
		for (OrderDetailRequest requestDetail : requestDetails) {
			Item item = Common.findItemById(requestDetail.getItemId(), itemRepository);

			OrderDetail orderDetail = OrderDetail.builder()
												 .unitPrice(item.getUnitPrice())
												 .quantity(requestDetail.getQuantity())
												 .item(item)
												 .build();

			details.add(orderDetail);

			totalPrice += item.getUnitPrice() * requestDetail.getQuantity();
		}
		return new Pair<>(totalPrice, details);
	}

	private void handleOrderItem(Order order) {
		List<Item> savedItems = new ArrayList<>();
		List<StockChangeHistory> savedHistories = new ArrayList<>();
		for (OrderDetail orderDetail : order.getOrderDetails()) {
			Item item = orderDetail.getItem();

			int quantityLeft = item.getQuantity() - orderDetail.getQuantity();
			if (quantityLeft < 0) {
				throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.QUANTITY_MIN);
			}

			item.setQuantity(quantityLeft);
			savedItems.add(item);

			StockChangeHistory history = StockChangeHistory.builder()
														   .item(item)
														   .type(StockChangeType.SELL)
														   .quantityLeft(quantityLeft)
														   .quantity(-orderDetail.getQuantity())
														   .build();
			savedHistories.add(history);
		}

		itemRepository.saveAll(savedItems);
		stockChangeHistoryRepository.saveAll(savedHistories);
	}

	private void handlePaybackItem(Order order) {
		List<Item> savedItems = new ArrayList<>();
		List<StockChangeHistory> savedHistories = new ArrayList<>();
		for (OrderDetail orderDetail : order.getOrderDetails()) {
			Item item = orderDetail.getItem();

			int quantityLeft = item.getQuantity() + orderDetail.getQuantity();

			item.setQuantity(quantityLeft);
			savedItems.add(item);

			StockChangeHistory history = StockChangeHistory.builder()
														   .item(item)
														   .type(StockChangeType.PAYBACK)
														   .quantityLeft(quantityLeft)
														   .quantity(orderDetail.getQuantity())
														   .build();
			savedHistories.add(history);
		}

		itemRepository.saveAll(savedItems);
		stockChangeHistoryRepository.saveAll(savedHistories);
	}

	private SimpleOrderResponse mapToSimpleDTO(Order order) {
		return SimpleOrderResponse.builder()
								  .id(order.getId())
								  .customer(mapToDTOCustomer(order.getCustomer()))
								  .staff(mapToDTOStaff(order.getStaff()))
								  .totalPrice(order.getTotalPrice())
								  .orderStatus(order.getStatus())
								  .createdAt(order.getCreatedAt())
								  .updatedAt(order.getUpdatedAt())
								  .build();
	}

	private OrderResponse mapToDTO(Order order) {
		return OrderResponse.builder()
							.id(order.getId())
							.customer(mapToDTOCustomer(order.getCustomer()))
							.staff(mapToDTOStaff(order.getStaff()))
							.totalPrice(order.getTotalPrice())
							.orderStatus(order.getStatus())
							.createdAt(order.getCreatedAt())
							.updatedAt(order.getUpdatedAt())
							.details(order.getOrderDetails().stream().map(this::mapToDTO).toList())
							.build();
	}

	private SimpleCustomerResponse mapToDTOCustomer(User user) {
		if (user == null) {
			return null;
		}
		return SimpleCustomerResponse.builder()
									 .id(user.getId())
									 .email(user.getEmail())
									 .phone(user.getUserAuth().getPhone())
									 .image(user.getImage())
									 .name(user.getName())
									 .build();
	}

	private SimpleStaffResponse mapToDTOStaff(User user) {
		if (user == null) {
			return null;
		}
		return SimpleStaffResponse.builder()
								  .id(user.getId())
								  .email(user.getEmail())
								  .image(user.getImage())
								  .name(user.getName())
								  .build();
	}

	private OrderDetailResponse mapToDTO(OrderDetail detail) {
		return OrderDetailResponse.builder()
								  .item(mapToDTO(detail.getItem()))
								  .quantity(detail.getQuantity())
								  .unitPrice(detail.getUnitPrice())
								  .totalSubPrice(detail.getQuantity() * detail.getUnitPrice())
								  .build();
	}

	private SimpleItemResponse mapToDTO(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .image(item.getImage())
								 .name(item.getName())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
