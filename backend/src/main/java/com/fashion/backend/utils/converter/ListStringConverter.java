package com.fashion.backend.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			return attribute == null ? "[]" : objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting list to JSON", e);
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		try {
			return dbData == null || dbData.isEmpty() ? new ArrayList<>() : objectMapper.readValue(dbData, List.class);
		} catch (IOException e) {
			throw new RuntimeException("Error converting JSON to list", e);
		}
	}
}
