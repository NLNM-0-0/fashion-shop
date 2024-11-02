package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.constant.StockChangeType;
import com.fashion.backend.entity.*;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.mail.MailSender;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.customer.SimpleCustomerResponse;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.order.*;
import com.fashion.backend.payload.staff.SimpleStaffResponse;
import com.fashion.backend.repository.*;
import com.fashion.backend.utils.AuthHelper;
import com.fashion.backend.utils.tuple.Pair;
import lombok.RequiredArgsConstructor;
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
	public void getCurrOrder() {
//		User customer = getCurrCustomer();
//
//
//
//		Pageable pageable = PageRequest.of(page.getPage() - 1,
//										   page.getLimit(),
//										   Sort.by(Sort.Direction.DESC, "createdAt"));
//		Specification<Notification> spec = filterNotifications(sender.getId(), filter);
//
//		Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);
//
//		List<Notification> notifications = notificationPage.getContent();
//
//		List<NotificationResponse> data = notifications.stream().map(this::mapToDTO).toList();
//
//		if (changeToSeen) {
//			notificationRepository.saveAll(notifications.stream()
//														.peek(notification -> notification.setSeen(true))
//														.toList());
//		}
//
//		return ListResponse.<NotificationResponse, NotificationFilter>builder()
//						   .data(data)
//						   .appPageResponse(AppPageResponse.builder()
//														   .index(page.getPage())
//														   .limit(page.getLimit())
//														   .totalPages(notificationPage.getTotalPages())
//														   .totalElements(notificationPage.getTotalElements())
//														   .build())
//						   .filter(filter)
//						   .build();
	}

	@Transactional
	public SimpleResponse receiveOrder(Long orderId) {
		Order order = Common.findOrderById(orderId, orderRepository);
		if (order.getStatus() == OrderStatus.DONE || order.getStatus() == OrderStatus.CANCELED) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.CAN_NOT_BE_REACHED_CLOSED_ORDER);
		}

		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		boolean isCustomerOrder = !Objects.equals(userAuth.getId(), order.getCustomer().getId());
		boolean isOrderMadeByStaff = order.getStaff() != null;
		if (isCustomerOrder || isOrderMadeByStaff) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.ORDER_JUST_CAN_BE_REACHED_BY_OWNER_CUSTOMER);
		}

		order.setStatus(OrderStatus.DONE);

		orderRepository.save(order);

		handleOrderItem(order);

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

		if (orderStatus == OrderStatus.DONE) {
			handleOrderItem(order);
		}

		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		User user = Common.findUserById(userAuth.getId(), userRepository);
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
	public OrderResponse staffPlaceOrder(PlaceOrderRequest request) {
		validateOrderDetails(request);

		User staff = getCurrStaff();

		Pair<Integer, List<OrderDetail>> priceCalculation = calcPrice(request.getDetails());
		int totalPrice = priceCalculation.first();
		List<OrderDetail> details = priceCalculation.second();

		Order order = Order.builder()
						   .staff(staff)
						   .customer(null)
						   .totalPrice(totalPrice)
						   .orderDetails(details)
						   .status(OrderStatus.DONE)
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

	private User getCurrStaff() {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.NORMAL_USER_CAN_NOT_CREATE_INVOICE_DIRECTLY);
		}
		return Common.findUserById(userAuth.getId(), userRepository);
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

	@Transactional
	public OrderResponse customerPlaceOrder(PlaceOrderRequest request) {
		validateOrderDetails(request);

		User customer = getCurrCustomer();

		Pair<Integer, List<OrderDetail>> priceCalculation = calcPrice(request.getDetails());
		int totalPrice = priceCalculation.first();
		List<OrderDetail> details = priceCalculation.second();

		Order order = Order.builder()
						   .staff(null)
						   .customer(customer)
						   .totalPrice(totalPrice)
						   .orderDetails(details)
						   .status(OrderStatus.PENDING)
						   .build();

		order = orderRepository.save(order);

		return mapToDTO(order);
	}

	private User getCurrCustomer() {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		if (!AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Order.STAFF_CAN_NOT_BUY_ITEM);
		}
		return Common.findUserById(userAuth.getId(), userRepository);
	}

	private OrderResponse mapToDTO(Order order) {
		return OrderResponse.builder()
							.id(order.getId())
							.customer(mapToDTOCustomer(order.getCustomer()))
							.staff(mapToDTOStaff(order.getStaff()))
							.totalPrice(order.getTotalPrice())
							.orderStatus(order.getStatus())
							.createdAt(order.getCreatedAt())
							.details(order.getOrderDetails().stream().map(this::mapToDTO).toList())
							.build();
	}

	private SimpleCustomerResponse mapToDTOCustomer(User user) {
		return SimpleCustomerResponse.builder()
									 .email(user.getEmail())
									 .phone(user.getUserAuth().getPhone())
									 .image(user.getImage())
									 .name(user.getName())
									 .build();
	}

	private SimpleStaffResponse mapToDTOStaff(User user) {
		return SimpleStaffResponse.builder()
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
