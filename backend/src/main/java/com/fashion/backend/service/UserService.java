package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.entity.UserGroup;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.feature.SimpleFeatureResponse;
import com.fashion.backend.payload.user.*;
import com.fashion.backend.payload.usergroup.UserGroupResponseWithoutHas;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.utils.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {
	private final UserAuthRepository userAuthRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public SimpleResponse changePassword(ChangePasswordRequest request) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		if (!passwordEncoder.matches(request.getOldPassword(), userDetails.getPassword())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.OLD_PASSWORD_NOT_CORRECT);
		}

		UserAuth user = Common.findUserAuthByEmail(email, userAuthRepository);

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userAuthRepository.save(user);

		return new SimpleResponse();
	}

	@Transactional
	public StaffProfileResponse seeStaffProfile() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();

		UserAuth userAuth = Common.findUserAuthByEmail(email, userAuthRepository);
		if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		User user = Common.findUserById(userAuth.getId(), userRepository);

		return mapToStaffDTO(user);
	}

	@Transactional
	public ProfileResponse seeProfile() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();

		UserAuth userAuth = Common.findUserAuthByEmail(email, userAuthRepository);
		if (!AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_STAFF);
		}

		User user = Common.findUserById(userAuth.getId(), userRepository);

		return mapToUserDTO(user);
	}

	@Transactional
	public StaffProfileResponse updateUserStaff(UpdateUserStaffRequest request) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		if (AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_CUSTOMER);
		}

		User user = Common.findUserById(userAuth.getId(), userRepository);

		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);

		return mapToStaffDTO(userRepository.save(user));
	}

	@Transactional
	public ProfileResponse updateUser(UpdateUserRequest request) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		if (!AuthHelper.isNormalUser(userAuth)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.CAN_NOT_REACH_STAFF);
		}

		User user = Common.findUserById(userAuth.getId(), userRepository);

		Common.updateIfNotNull(request.getName(), user::setName);
		Common.updateIfNotNull(request.getEmail(), user::setEmail);
		Common.updateIfNotNull(request.getDob(), user::setDob);
		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);
		Common.updateIfNotNull(request.getMale(), user::setMale);

		return mapToUserDTO(userRepository.save(user));
	}

	private StaffProfileResponse mapToStaffDTO(User user) {
		return StaffProfileResponse.builder()
								   .id(user.getUserAuth().getId())
								   .name(user.getName())
								   .email(user.getUserAuth().getEmail())
								   .image(user.getImage())
								   .address(user.getAddress())
								   .male(user.isMale())
								   .dob(user.getDob())
								   .userGroup(mapToStaffDTO(user.getUserAuth().getUserGroup()))
								   .build();
	}

	private ProfileResponse mapToUserDTO(User user) {
		return ProfileResponse.builder()
							  .id(user.getUserAuth().getId())
							  .name(user.getName())
							  .email(user.getEmail())
							  .phone(user.getUserAuth().getPhone())
							  .image(user.getImage())
							  .address(user.getAddress())
							  .male(user.isMale())
							  .dob(user.getDob())
							  .build();
	}

	private UserGroupResponseWithoutHas mapToStaffDTO(UserGroup userGroup) {
		return UserGroupResponseWithoutHas.builder()
										  .id(userGroup.getId())
										  .name(userGroup.getName())
										  .features(userGroup.getUserGroupFeatures()
															 .stream()
															 .map(userGroupFeature -> SimpleFeatureResponse.builder()
																										   .id(userGroupFeature.getFeature()
																															   .getId())
																										   .code(userGroupFeature.getFeature()
																																 .getCode())
																										   .name(userGroupFeature.getFeature()
																																 .getName())
																										   .build())
															 .toList())
										  .build();
	}
}
