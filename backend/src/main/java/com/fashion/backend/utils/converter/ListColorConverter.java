package com.fashion.backend.utils.converter;

import com.fashion.backend.constant.Color;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ListColorConverter implements AttributeConverter<List<Color>, String> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<Color> attribute) {
		try {
			if (attribute == null || attribute.isEmpty()) {
				return "[]";
			}
			List<String> colorNames = attribute.stream().map(Color::name).collect(Collectors.toList());
			return objectMapper.writeValueAsString(colorNames);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting color list to JSON", e);
		}
	}

	@Override
	public List<Color> convertToEntityAttribute(String dbData) {
		try {
			if (dbData == null || dbData.isEmpty()) {
				return new ArrayList<>();
			}
			List<String> colorNames = objectMapper.readValue(dbData, List.class);
			return colorNames.stream().map(Color::fromString).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("Error converting JSON to color list", e);
		}
	}
}
