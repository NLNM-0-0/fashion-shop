package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.OTP;
import com.fashion.backend.entity.PasswordResetToken;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.mail.MailSender;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.auth.*;
import com.fashion.backend.repository.OtpRepository;
import com.fashion.backend.repository.PasswordResetTokenRepository;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.sms.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final OtpRepository otpRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTService jwtService;
	private final AuthenticationManager authenticationManager;
	private final MailSender mailSender;
	private final SmsSender smsSender;

	@Transactional
	public AuthenticationResponse authenticateByPhone(PhoneAuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getPhone(),
						request.getPassword()
				)
		);

		UserAuth userAuth = Common.findAvailableUserAuth(request.getPhone(), false, userAuthRepository);
		String jwtToken = jwtService.generateToken(userAuth);
		return AuthenticationResponse.builder().token(jwtToken).expired(jwtService.extractExpiration(jwtToken)).build();
	}

	@Transactional
	public AuthenticationResponse authenticateByEmail(EmailAuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
		);

		UserAuth userAuth = Common.findAvailableUserAuth(request.getEmail(), true, userAuthRepository);
		String jwtToken = jwtService.generateToken(userAuth);
		return AuthenticationResponse.builder().token(jwtToken).expired(jwtService.extractExpiration(jwtToken)).build();
	}

	@Transactional
	public SimpleResponse sendEmailToResetPassword(EmailRequest request) {
		UserAuth userAuth = Common.findUserAuthByEmail(request.getEmail(), userAuthRepository);

		String token = UUID.randomUUID().toString();

		passwordResetTokenRepository.save(new PasswordResetToken(token, userAuth));

		passwordResetEmailLink(userAuth, token);
		return new SimpleResponse();
	}

	private void passwordResetEmailLink(UserAuth user, String token) {
		String url = generateResetPasswordUrl(token);

		mailSender.sendResetPasswordEmail(url, user);
	}

	private String generateResetPasswordUrl(String token) {
		return ApplicationConst.FE_URL + ApplicationConst.RESET_PASSWORD_FE_PATH + token;
	}

	@Transactional
	public SimpleResponse registerUser(RegisterRequest request) {
		Optional<UserAuth> userAuthOptional = userAuthRepository.findByPhone(request.getPhone());
		if (userAuthOptional.isPresent()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_EXIST);
		}

		Optional<User> userOptional = userRepository.findFirstByEmail(request.getEmail());
		if (userOptional.isPresent()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_EXIST);
		}

		handleImage(request);

		UserAuth userAuth = UserAuth.builder()
									.phone(request.getPhone())
									.password(passwordEncoder.encode(request.getPassword()))
									.isDeleted(false)
									.isVerified(false)
									.isAdmin(false)
									.build();
		userAuth = userAuthRepository.save(userAuth);

		User user = User.builder()
						.email(request.getEmail())
						.name(request.getName())
						.phone(null)
						.image(request.getImage())
						.address(request.getAddress())
						.male(request.getMale())
						.dob(request.getDob())
						.userAuth(userAuth)
						.build();

		userRepository.save(user);

		return new SimpleResponse();
	}

	private void handleImage(RegisterRequest request) {
		if (request.getImage().isEmpty()) {
			request.setImage(ApplicationConst.DEFAULT_AVATAR);
		}
	}

	@Transactional
	public SimpleResponse resetPasswordByEmail(ResetPasswordRequest request, String token) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
																			.orElseThrow(() -> new AppException(
																					HttpStatus.BAD_REQUEST,
																					Message.TOKEN_NOT_EXIST));
		if (!passwordResetToken.isValid()) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, Message.TOKEN_EXPIRED);
		}
		UserAuth userAuth = passwordResetToken.getUser();
		userAuth.setPassword(passwordEncoder.encode(request.getPassword()));

		userAuthRepository.save(userAuth);
		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse sendOtpToResetPassword(PhoneRequest request) {
		UserAuth userAuth = Common.findUserAuthByPhone(request.getPhone(), userAuthRepository);
		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		Optional<OTP> otp = otpRepository.findByUser(user.getId());

		String otpNumber = OTP.generateOTP();
		OTP newOtp;

		if (otp.isPresent() && !userAuth.isVerified()) {
			newOtp = otp.get();
			newOtp.increaseRetry();
			newOtp.setOtp(otpNumber);
		} else {
			newOtp = new OTP(otpNumber, userAuth);

			userAuth.setVerified(false);
			userAuthRepository.save(userAuth);
		}

//		if (!smsSender.sendOtp(userAuth.getPhone(), otpNumber)) {
//			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, Message.COMMON_ERR);
//		}

		otpRepository.save(newOtp);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse verifiedOTP(OtpVerifyRequest request) {
		UserAuth userAuth = Common.findUserAuthByPhone(request.getPhone(), userAuthRepository);
		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		OTP otp = otpRepository.findByUser(user.getId())
							   .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, Message.OTP_NOT_EXIST));

		if (!otp.isValid()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.OTP_EXPIRED);
		}

		userAuth.setVerified(true);
		userAuthRepository.save(userAuth);

		otpRepository.delete(otp);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse resetPasswordByOTP(OtpResetPasswordRequest request) {
		UserAuth userAuth = Common.findUserAuthByPhone(request.getPhone(), userAuthRepository);
		User user = Common.findUserByUserAuth(userAuth.getId(), userRepository);

		OTP otp = otpRepository.findByUser(user.getId())
							   .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, Message.OTP_NOT_EXIST));

		if (!otp.isValid()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.OTP_EXPIRED);
		}

		userAuth.setPassword(passwordEncoder.encode(request.getPassword()));
		userAuth.setVerified(true);

		userAuthRepository.save(userAuth);

		return new SimpleResponse();
	}
}
