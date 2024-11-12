package com.fashion.backend.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fashion.backend.entity.ItemImage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ItemImageListConverter implements AttributeConverter<List<ItemImage>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ItemImage> itemImages) {
        try {
            return objectMapper.writeValueAsString(itemImages);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting list of ItemImage to JSON", e);
        }
    }

    @Override
    public List<ItemImage> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, ItemImage.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to list of ItemImage", e);
        }
    }
}
