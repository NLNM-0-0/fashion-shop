package com.fashion.backend.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fashion.backend.entity.ItemColor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ItemColorListConverter implements AttributeConverter<List<ItemColor>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ItemColor> itemColors) {
        try {
            return objectMapper.writeValueAsString(itemColors);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting list of ItemColor to JSON", e);
        }
    }

    @Override
    public List<ItemColor> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, ItemColor.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to list of ItemColor", e);
        }
    }
}
