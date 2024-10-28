package com.fashion.backend.repository;

import com.fashion.backend.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
	@Query("SELECT f FROM Feature f WHERE f.code <> 'ADMIN'")
	List<Feature> findAllNotAdmin();
}
