package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.ListResponseWithoutFilter;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.payload.staff.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService {
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public StaffResponse createStaff(CreateStaffRequest request) {
		Optional<UserAuth> checkUserAuth = userAuthRepository.findByEmail(request.getEmail());
		if (checkUserAuth.isPresent()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_EXIST);
		}

		UserAuth userAuth = UserAuth.builder()
									.email(request.getEmail())
									.password(passwordEncoder.encode(ApplicationConst.DEFAULT_PASSWORD))
									.isVerified(true)
									.isDeleted(false)
									.build();
		userAuthRepository.save(userAuth);

		handleImage(request);
		User user = mapToEntity(request);
		user.setEmail(null);

		user.setUserAuth(userAuth);

		return mapToDTO(userRepository.save(user));
	}

	private void handleImage(CreateStaffRequest request) {
		if (request.getImage().isEmpty()) {
			request.setImage(ApplicationConst.DEFAULT_AVATAR);
		}
	}

	@Transactional
	public StaffResponse updateStaff(Long userId, UpdateStaffRequest request) {
		User user = Common.findUserById(userId, userRepository);

		UserAuth userAuth = user.getUserAuth();
		String currEmail = Common.getCurrUserName();
		if (Objects.equals(userAuth.getEmail(), currEmail)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_UPDATE_YOURSELF);
		} else if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		Common.updateIfNotNull(request.getAdmin(), userAuth::setAdmin);
		userAuthRepository.save(userAuth);

		Common.updateIfNotNull(request.getName(), user::setName);
		Common.updateIfNotNull(request.getDob(), user::setDob);
		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);
		Common.updateIfNotNull(request.getMale(), user::setMale);

		return mapToDTO(user);
	}

	@Transactional
	public SimpleResponse deleteStaff(Long userId) {
		User user = Common.findUserById(userId, userRepository);

		String email = Common.getCurrUserName();
		if (Objects.equals(user.getUserAuth().getEmail(), email)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_DELETE_YOURSELF);
		} else if (AuthHelper.isNormalUser(user.getUserAuth())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		userAuthRepository.deleteUserAuthById(user.getUserAuth().getId());

		return new SimpleResponse();
	}

	@Transactional
	public ListResponseWithoutFilter<StaffResponse> getAllStaff(AppPageRequest page) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));

		Page<User> userPage = userRepository.findAllNotHasPhone(Specification.where(null), pageable);

		List<User> users = userPage.getContent();

		List<StaffResponse> data = users.stream().map(this::mapToDTO).toList();

		return ListResponseWithoutFilter.<StaffResponse>builder()
										.data(data)
										.appPageResponse(AppPageResponse.builder()
																		.index(page.getPage())
																		.limit(page.getLimit())
																		.totalPages(userPage.getTotalPages())
																		.totalElements(userPage.getTotalElements())
																		.build())
										.build();
	}


	@Transactional
	public ListResponse<StaffResponse, StaffFilter> getStaffs(AppPageRequest page, StaffFilter filter) {
		String email = Common.getCurrUserName();

		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterStaffs(filter);

		Page<User> userPage = userRepository.findAllNotHasPhoneAndNotDeleteAndNotHaveEmail(email, spec, pageable);

		List<User> users = userPage.getContent();

		List<StaffResponse> data = users.stream().map(this::mapToDTO).toList();

		return ListResponse.<StaffResponse, StaffFilter>builder()
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
	public StaffResponse getStaff(Long id) {
		String email = Common.getCurrUserName();

		User user = Common.findUserById(id, userRepository);
		if (Objects.equals(user.getUserAuth().getEmail(), email)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_SEE_DETAIL_YOURSELF);
		} else if (AuthHelper.isNormalUser(user.getUserAuth())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		return mapToDTO(user);
	}

	private Specification<User> filterStaffs(StaffFilter filter) {
		Specification<User> spec = Specification.where(null);
		if (filter.getName() != null) {
			spec = StaffSpecs.hasName(filter.getName());
		}
		if (filter.getEmail() != null) {
			spec = spec.and(StaffSpecs.hasEmail(filter.getEmail()));
		}
		if (filter.getAdmin() != null) {
			spec = spec.and(StaffSpecs.isAdmin(filter.getAdmin()));
		}
		if (filter.getMale() != null) {
			spec = spec.and(StaffSpecs.isMale(filter.getMale()));
		}
		if (filter.getMonthDOB() != null) {
			spec = spec.and(StaffSpecs.hasDOBinMonth(filter.getMonthDOB()));
		}
		if (filter.getYearDOB() != null) {
			spec = spec.and(StaffSpecs.hasDOBinYear(filter.getYearDOB()));
		}
		return spec;
	}

	private StaffResponse mapToDTO(User user) {
		return StaffResponse.builder()
							.id(user.getId())
							.name(user.getName())
							.email(user.getUserAuth().getEmail())
							.image(user.getImage())
							.dob(user.getDob())
							.address(user.getAddress())
							.male(user.isMale())
							.admin(user.getUserAuth().isAdmin())
							.build();
	}

	private User mapToEntity(CreateStaffRequest request) {
		return User.builder()
				   .name(request.getName())
				   .email(request.getEmail())
				   .address(request.getAddress())
				   .dob(request.getDob())
				   .image(request.getImage())
				   .male(request.getMale())
				   .build();
	}
}
