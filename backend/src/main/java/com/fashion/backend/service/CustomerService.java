package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.customer.CustomerFilter;
import com.fashion.backend.payload.customer.CustomerResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.payload.staff.StaffSpecs;
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
		UserAuth userAuth = Common.findUserAuthById(customerId, userAuthRepository);

		if (userAuth.getEmail().equals(ApplicationConst.ADMIN_EMAIL)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_DELETE_ADMIN);
		} else if (!AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_STAFF);
		}

		userAuthRepository.deleteUserById(customerId);

		return new SimpleResponse();
	}

	@Transactional
	public ListResponse<CustomerResponse, CustomerFilter> getAllCustomer(AppPageRequest page, CustomerFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterCustomers(filter);

		Page<User> userPage = userRepository.findAll(spec, pageable);

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
	public ListResponse<CustomerResponse, CustomerFilter> getStaffs(AppPageRequest page, CustomerFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterCustomers(filter);

		Page<User> userPage = userRepository.findAllHasPhoneAndNotDelete(spec, pageable);

		return getListResponseFromPage(userPage, page, filter);
	}

	@Transactional
	public CustomerResponse getCustomer(Long id) {
		UserAuth userAuth = Common.findUserAuthById(id, userAuthRepository);

		if (userAuth.getEmail().equals(ApplicationConst.ADMIN_EMAIL)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_ADMIN);
		} else if (!AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_STAFF);
		}

		User user = Common.findUserById(id, userRepository);

		return mapToDTO(user);
	}

	private Specification<User> filterCustomers(CustomerFilter filter) {
		Specification<User> spec = Specification.where(null);
		if (filter.getName() != null) {
			spec = StaffSpecs.hasName(filter.getName());
		}
		if (filter.getEmail() != null) {
			spec = spec.and(StaffSpecs.hasEmail(filter.getEmail()));
		}
		if (filter.getPhone() != null) {
			spec = spec.and(StaffSpecs.hasPhone(filter.getPhone()));
		}
		return spec;
	}

	private CustomerResponse mapToDTO(User user) {
		return CustomerResponse.builder()
							   .id(user.getUserAuth().getId())
							   .name(user.getName())
							   .email(user.getEmail())
							   .image(user.getImage())
							   .phone(user.getPhone())
							   .dob(user.getDob())
							   .address(user.getAddress())
							   .male(user.isMale())
							   .build();
	}
}
