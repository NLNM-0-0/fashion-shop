package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.entity.UserGroup;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.payload.staff.*;
import com.fashion.backend.payload.usergroup.SimpleUserGroupResponse;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserGroupRepository;
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

@Service
@RequiredArgsConstructor
public class StaffService {
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;
	private final UserGroupRepository userGroupRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public StaffResponse createStaff(CreateStaffRequest request) {
		UserGroup userGroup = Common.findUserGroupById(request.getUserGroup(), userGroupRepository);

		UserAuth userAuth = UserAuth.builder()
									.email(request.getEmail())
									.password(passwordEncoder.encode(ApplicationConst.DEFAULT_PASSWORD))
									.isVerified(true)
									.isDeleted(false)
									.userGroup(userGroup)
									.build();

		userAuth = userAuthRepository.save(userAuth);

		handleImage(request);
		User user = mapToEntity(request);
		user.setEmail(null);
		user.setUserAuth(userAuth);
		user.setId(userAuth.getId());

		return mapToDTO(userRepository.save(user));
	}

	private void handleImage(CreateStaffRequest request) {
		if (request.getImage().isEmpty()) {
			request.setImage(ApplicationConst.DEFAULT_AVATAR);
		}
	}

	@Transactional
	public StaffResponse updateStaff(Long userId, UpdateStaffRequest request) {
		UserAuth userAuth = Common.findUserAuthById(userId, userAuthRepository);
		if (userAuth.getEmail().equals(ApplicationConst.ADMIN_EMAIL)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_ADMIN);
		} else if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		User user = Common.findUserById(userId, userRepository);

		Common.updateIfNotNull(request.getName(), user::setName);
		Common.updateIfNotNull(request.getDob(), user::setDob);
		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);
		Common.updateIfNotNull(request.getMale(), user::setMale);

		updateUserGroup(userAuth, request.getUserGroup());

		userAuthRepository.save(userAuth);

		return mapToDTO(userRepository.save(user));
	}

	private void updateUserGroup(UserAuth userAuth, Long updatedUserGroupId) {
		if (updatedUserGroupId != null) {
			UserGroup userGroup = Common.findUserGroupById(updatedUserGroupId, userGroupRepository);
			userAuth.setUserGroup(userGroup);
		}
	}

	@Transactional
	public SimpleResponse deleteStaff(Long userId) {
		UserAuth userAuth = Common.findUserAuthById(userId, userAuthRepository);

		if (userAuth.getEmail().equals(ApplicationConst.ADMIN_EMAIL)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_DELETE_ADMIN);
		} else if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		userAuthRepository.deleteUserById(userId);

		return new SimpleResponse();
	}

	@Transactional
	public ListResponse<StaffResponse, StaffFilter> getAllStaff(AppPageRequest page, StaffFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterStaffs(filter);

		Page<User> userPage = userRepository.findAll(spec, pageable);

		return getListResponseFromPage(userPage, page, filter);
	}

	private ListResponse<StaffResponse, StaffFilter> getListResponseFromPage(Page<User> userPage,
																			 AppPageRequest page,
																			 StaffFilter filter) {
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
	public ListResponse<StaffResponse, StaffFilter> getStaffs(AppPageRequest page, StaffFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<User> spec = filterStaffs(filter);

		Page<User> userPage = userRepository.findAllNotHasPhoneAndNotDeleteAndNotAdmin(spec, pageable);

		return getListResponseFromPage(userPage, page, filter);
	}

	@Transactional
	public StaffResponse getStaff(Long id) {
		UserAuth userAuth = Common.findUserAuthById(id, userAuthRepository);

		if (userAuth.getEmail().equals(ApplicationConst.ADMIN_EMAIL)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_ADMIN);
		} else if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		User user = Common.findUserById(id, userRepository);

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
		if (filter.getPhone() != null) {
			spec = spec.and(StaffSpecs.hasPhone(filter.getPhone()));
		}
		if (filter.getUserGroup() != null) {
			spec = spec.and(StaffSpecs.hasUserGroupName(filter.getUserGroup()));
		}
		if (filter.getUserGroupId() != null) {
			spec = spec.and(StaffSpecs.hasUserGroupId(filter.getUserGroupId()));
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
							.id(user.getUserAuth().getId())
							.name(user.getName())
							.email(user.getEmail())
							.userGroup(mapToSimpleUserGroup(user.getUserAuth().getUserGroup()))
							.image(user.getImage())
							.dob(user.getDob())
							.address(user.getAddress())
							.male(user.isMale())
							.build();
	}

	private SimpleUserGroupResponse mapToSimpleUserGroup(UserGroup userGroup) {
		if (userGroup == null) {
			return null;
		}
		return SimpleUserGroupResponse.builder()
									  .id(userGroup.getId())
									  .name(userGroup.getName())
									  .build();
	}

	private User mapToEntity(CreateStaffRequest request) {
		return User.builder()
				   .name(request.getName())
				   .email(request.getEmail())
				   .address(request.getAddress())
				   .dob(request.getDob())
				   .phone(request.getPhone())
				   .image(request.getImage())
				   .male(request.getMale())
				   .build();
	}
}
