package com.fashion.backend.exception;

import com.fashion.backend.constant.Message;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private ResponseEntity<ErrorResponse> createErrorResponse(Exception exception,
															  WebRequest webRequest,
															  HttpStatus status) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(),
														status,
														exception.getMessage(),
														webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, status);
	}

	private ResponseEntity<ErrorResponse> createErrorResponse(String message,
															  WebRequest webRequest,
															  HttpStatus status) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(),
														status,
														message,
														webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleRequestException(AppException exception,
																WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, exception.getStatus());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
																		 WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DataIntegrityViolationException exception,
																	 WebRequest webRequest) {
		String exceptionMessage = exception.getMessage();
		if (exceptionMessage.contains("Duplicate")) {
			exceptionMessage = exceptionMessage.substring(exceptionMessage.indexOf("key") + 5);
			String key = exceptionMessage.substring(exceptionMessage.indexOf(".") + 1, exceptionMessage.indexOf("'"));
			return createErrorResponse(String.format("%s already exists in the system", key),
									   webRequest,
									   HttpStatus.BAD_REQUEST);
		} else if (exceptionMessage.contains("not-null")) {
			return createErrorResponse("A field is missing in the request",
									   webRequest,
									   HttpStatus.BAD_REQUEST);
		}
		return createErrorResponse("An error occurred with the database. Please try again",
								   webRequest,
								   HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleRequestException(BadCredentialsException exception,
																WebRequest webRequest) {
		return createErrorResponse(Message.Auth.USER_NOT_CORRECT, webRequest, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(
			InternalAuthenticationServiceException exception,
			WebRequest webRequest) {
		Throwable cause = exception.getCause();
		if (cause instanceof AppException appException) {
			if (appException.getStatus() == HttpStatus.FORBIDDEN) {
				return createErrorResponse(appException.getMessage(), webRequest, appException.getStatus());
			}
		}
		return createErrorResponse(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception,
																	 WebRequest webRequest) {
		return createErrorResponse(Message.USER_NOT_HAVE_FEATURE, webRequest, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception,
															   WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
															   @NonNull HttpHeaders headers,
															   @NonNull HttpStatusCode status,
															   @NonNull WebRequest request) {
		HashMap<String, String> errors = new HashMap<>();
		for (ObjectError error : ex.getAllErrors()) {
			String fieldName = ((FieldError) error).getField();
			String fieldMessage = error.getDefaultMessage();
			errors.put(fieldName, fieldMessage);
		}

		StringBuilder concatenated = new StringBuilder();
		for (String value : errors.values()) {
			concatenated.append(value).append(". ");
		}
		if (!concatenated.isEmpty()) {
			concatenated.setLength(concatenated.length() - 2);
		}
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorResponse errorResponse = new ErrorResponse(new Date(),
														httpStatus,
														concatenated.toString(),
														request.getDescription(false));
		return new ResponseEntity<>(errorResponse, httpStatus);
	}
}
