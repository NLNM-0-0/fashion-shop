package com.fashion.backend.repository;

import com.fashion.backend.entity.IdClass.UserGroupFeatureId;
import com.fashion.backend.entity.UserGroupFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupFeatureRepository extends JpaRepository<UserGroupFeature, UserGroupFeatureId> {
	Optional<UserGroupFeature> findFirstByFeatureId(Long featureId);

	List<UserGroupFeature> findByUserGroupId(Long userGroupId);
}
