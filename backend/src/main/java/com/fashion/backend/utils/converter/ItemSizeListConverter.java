package com.fashion.backend.utils.converter;

import com.fashion.backend.entity.ItemSize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class ItemSizeListConverter implements AttributeConverter<List<ItemSize>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<ItemSize> itemSizes) {
		try {
			return objectMapper.writeValueAsString(itemSizes);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting list of ItemSize to JSON", e);
		}
	}

	@Override
	public List<ItemSize> convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData,
										  objectMapper.getTypeFactory()
													  .constructCollectionType(List.class, ItemSize.class));
		} catch (IOException e) {
			throw new IllegalArgumentException("Error converting JSON to list of ItemSize", e);
		}
	}
}
