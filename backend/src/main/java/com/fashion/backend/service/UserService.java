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

		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		userAuth.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userAuthRepository.save(userAuth);


		return new SimpleResponse();
	}

	@Transactional
	public StaffProfileResponse seeStaffProfile() {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		return mapToStaffDTO(user);
	}

	@Transactional
	public ProfileResponse seeProfile() {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		return mapToUserDTO(user);
	}

	@Transactional
	public StaffProfileResponse updateUserStaff(UpdateUserStaffRequest request) {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Common.updateIfNotNull(request.getAddress(), user::setAddress);
		Common.updateIfNotNull(request.getImage(), user::setImage);

		return mapToStaffDTO(userRepository.save(user));
	}

	@Transactional
	public ProfileResponse updateUser(UpdateUserRequest request) {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
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

		return mapToUserDTO(userRepository.save(user));
	}

	private StaffProfileResponse mapToStaffDTO(User user) {
		return StaffProfileResponse.builder()
								   .id(user.getId())
								   .name(user.getName())
								   .email(user.getEmail())
								   .image(user.getImage())
								   .address(user.getAddress())
								   .male(user.isMale())
								   .dob(user.getDob())
								   .admin(user.getUserAuth().isAdmin())
								   .build();
	}

	private ProfileResponse mapToUserDTO(User user) {
		return ProfileResponse.builder()
							  .id(user.getId())
							  .name(user.getName())
							  .email(user.getEmail())
							  .phone(user.getUserAuth().getPhone())
							  .image(user.getImage())
							  .address(user.getAddress())
							  .male(user.isMale())
							  .dob(user.getDob())
							  .build();
	}
}
