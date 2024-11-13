package com.fashion.backend.service;

import com.fashion.backend.constant.Color;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.item.ItemColorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ColorService {
	@Transactional
	public SimpleListResponse<ItemColorDTO> getColors() {
		return SimpleListResponse.<ItemColorDTO>builder()
								 .data(Arrays.stream(Color.values()).map(this::mapToDTO).toList())
								 .build();
	}

	private ItemColorDTO mapToDTO(Color color) {
		return ItemColorDTO.builder()
						   .name(color.name())
						   .hex(color.getHexValue())
						   .build();
	}
}
