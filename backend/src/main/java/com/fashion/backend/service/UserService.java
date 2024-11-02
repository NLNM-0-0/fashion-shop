package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.user.*;
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

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {
	private final UserAuthRepository userAuthRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public SimpleResponse changePassword(ChangePasswordRequest request) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!passwordEncoder.matches(request.getOldPassword(), userDetails.getPassword())) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.OLD_PASSWORD_NOT_CORRECT);
		}

		String username = userDetails.getUsername();
		UserAuth userAuth;
		if (AuthHelper.isStaff(username)) {
			userAuth = Common.findUserAuthByEmail(username, userAuthRepository);
		} else {
			userAuth = Common.findUserAuthByPhone(username, userAuthRepository);
		}
		userAuth.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userAuthRepository.save(userAuth);


		return new SimpleResponse();
	}

	@Transactional
	public StaffProfileResponse seeStaffProfile() {
		String email = Common.getCurrUserName();

		UserAuth userAuth = Common.findUserAuthByEmail(email, userAuthRepository);

		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		return mapToStaffDTO(user, userAuth);
	}

	@Transactional
	public ProfileResponse seeProfile() {
		String phone = Common.getCurrUserName();

		UserAuth userAuth = Common.findUserAuthByPhone(phone, userAuthRepository);

		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		return mapToUserDTO(user, userAuth);
	}

	@Transactional
	public StaffProfileResponse updateUserStaff(UpdateUserStaffRequest request) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);

		return mapToStaffDTO(userRepository.save(user), userAuth);
	}

	@Transactional
	public ProfileResponse updateUser(UpdateUserRequest request) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		if (request.getEmail() != null) {
			Optional<User> checkedUser = userRepository.findFirstByEmail(request.getEmail());
			if (checkedUser.isPresent()) {
				throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_EXIST);
			}
		}

		Common.updateIfNotNull(request.getName(), user::setName);
		Common.updateIfNotNull(request.getEmail(), user::setEmail);
		Common.updateIfNotNull(request.getDob(), user::setDob);
		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);
		Common.updateIfNotNull(request.getMale(), user::setMale);

		return mapToUserDTO(userRepository.save(user), userAuth);
	}

	private StaffProfileResponse mapToStaffDTO(User user, UserAuth userAuth) {
		return StaffProfileResponse.builder()
								   .id(user.getId())
								   .name(user.getName())
								   .email(userAuth.getEmail())
								   .image(user.getImage())
								   .address(user.getAddress())
								   .male(user.isMale())
								   .dob(user.getDob())
								   .admin(userAuth.isAdmin())
								   .build();
	}

	private ProfileResponse mapToUserDTO(User user, UserAuth userAuth) {
		return ProfileResponse.builder()
							  .id(user.getId())
							  .name(user.getName())
							  .email(user.getEmail())
							  .phone(userAuth.getPhone())
							  .image(user.getImage())
							  .address(user.getAddress())
							  .male(user.isMale())
							  .dob(user.getDob())
							  .build();
	}
}
