package com.fashion.backend.utils.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DDMMYYYYFormatValidator implements ConstraintValidator<ValidDDMMYYYYFormat, String> {
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Override
	public void initialize(ValidDDMMYYYYFormat constraintAnnotation) {
	}

	@Override
	public boolean isValid(String dob, ConstraintValidatorContext context) {
		if (dob == null || dob.isEmpty()) {
			return true;
		}
		try {
			LocalDate.parse(dob, dateFormatter);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
