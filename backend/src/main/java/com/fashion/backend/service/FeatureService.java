package com.fashion.backend.service;

import com.fashion.backend.entity.Feature;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.feature.SimpleFeatureResponse;
import com.fashion.backend.repository.FeatureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureService {
	private final FeatureRepository featureRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public SimpleListResponse<SimpleFeatureResponse> getAllFeatures() {
		List<SimpleFeatureResponse> features = featureRepository.findAllNotAdmin()
																.stream()
																.map(this::mapToDTO)
																.toList();
		return new SimpleListResponse<>(features);
	}

	private SimpleFeatureResponse mapToDTO(Feature feature) {
		return objectMapper.convertValue(feature, SimpleFeatureResponse.class);
	}
}
