package com.fashion.backend.utils.converter;

import com.fashion.backend.entity.ItemSize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ListItemSizeConverter implements AttributeConverter<List<ItemSize>, String> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<ItemSize> attribute) {
		try {
			return attribute == null ? "[]" : objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting list to JSON", e);
		}
	}

	@Override
	public List<ItemSize> convertToEntityAttribute(String dbData) {
		try {
			return dbData == null || dbData.isEmpty() ?
				   new ArrayList<>() :
				   objectMapper.readValue(dbData,
										  objectMapper.getTypeFactory()
													  .constructCollectionType(List.class, ItemSize.class));
		} catch (IOException e) {
			throw new RuntimeException("Error converting JSON to list", e);
		}
	}
}