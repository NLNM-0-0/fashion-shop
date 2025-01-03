package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.customer.CustomerFilter;
import com.fashion.backend.payload.customer.CustomerResponse;
import com.fashion.backend.payload.customer.CustomerSpecs;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.utils.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;

	@Transactional
	public SimpleResponse deleteCustomer(Long customerId) {
		User user = Common.findUserById(customerId, userRepository);

		UserAuth userAuth = user.getUserAuth();

		if (AuthHelper.isStaff(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_STAFF);
		}

		userAuth.setDeleted(true);

		userAuthRepository.save(userAuth);

		return new SimpleResponse();
	}

	@Transactional
	public ListResponse<CustomerResponse, CustomerFilter> getAllCustomer(AppPageRequest page, CustomerFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterCustomers(filter);

		Page<User> userPage = userRepository.findAllHasPhone(spec, pageable);

		return getListResponseFromPage(userPage, page, filter);
	}

	private ListResponse<CustomerResponse, CustomerFilter> getListResponseFromPage(Page<User> userPage,
																				   AppPageRequest page,
																				   CustomerFilter filter) {
		List<User> users = userPage.getContent();

		List<CustomerResponse> data = users.stream().map(this::mapToDTO).toList();

		return ListResponse.<CustomerResponse, CustomerFilter>builder()
						   .data(data)
						   .appPageResponse(AppPageResponse.builder()
														   .index(page.getPage())
														   .limit(page.getLimit())
														   .totalPages(userPage.getTotalPages())
														   .totalElements(userPage.getTotalElements())
														   .build())
						   .filter(filter)
						   .build();
	}


	@Transactional
	public ListResponse<CustomerResponse, CustomerFilter> getCustomers(AppPageRequest page, CustomerFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterCustomers(filter);

		Page<User> userPage = userRepository.findAllHasPhoneAndNotDelete(spec, pageable);

		return getListResponseFromPage(userPage, page, filter);
	}

	@Transactional
	public CustomerResponse getCustomer(Long id) {
		User user = Common.findUserById(id, userRepository);

		if (AuthHelper.isStaff(user.getUserAuth())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_STAFF);
		}

		return mapToDTO(user);
	}

	private Specification<User> filterCustomers(CustomerFilter filter) {
		Specification<User> spec = Specification.where(null);
		if (filter.getName() != null) {
			spec = CustomerSpecs.hasName(filter.getName());
		}
		if (filter.getEmail() != null) {
			spec = spec.and(CustomerSpecs.hasEmail(filter.getEmail()));
		}
		if (filter.getPhone() != null) {
			spec = spec.and(CustomerSpecs.hasPhone(filter.getPhone()));
		}
		return spec;
	}

	private CustomerResponse mapToDTO(User user) {
		return CustomerResponse.builder()
							   .id(user.getId())
							   .name(user.getName())
							   .email(user.getEmail())
							   .image(user.getImage())
							   .phone(user.getUserAuth().getPhone())
							   .dob(user.getDob())
							   .address(user.getAddress())
							   .male(user.isMale())
							   .build();
	}
}
