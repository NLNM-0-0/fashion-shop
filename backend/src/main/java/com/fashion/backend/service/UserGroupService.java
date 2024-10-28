package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Feature;
import com.fashion.backend.entity.UserGroup;
import com.fashion.backend.entity.UserGroupFeature;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.feature.FeatureResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.payload.usergroup.*;
import com.fashion.backend.repository.FeatureRepository;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserGroupFeatureRepository;
import com.fashion.backend.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserGroupService {
	private final UserAuthRepository userAuthRepository;
	private final UserGroupRepository userGroupRepository;
	private final FeatureRepository featureRepository;
	private final UserGroupFeatureRepository UserGroupFeatureRepository;

	@Transactional
	public ListResponse<SimpleUserGroupResponse, UserGroupFilter> getUserGroups(AppPageRequest page,
																				UserGroupFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1, page.getLimit(), Sort.by(Sort.Direction.DESC, "name"));
		Specification<UserGroup> spec = filterUserGroups(filter);

		Page<UserGroup> userGroupPage = userGroupRepository.findAll(spec, pageable);

		List<UserGroup> userGroups = userGroupPage.getContent();

		List<SimpleUserGroupResponse> data = userGroups.stream().map(this::mapToSimpleDTO).toList();

		return ListResponse.<SimpleUserGroupResponse, UserGroupFilter>builder()
						   .data(data)
						   .appPageResponse(AppPageResponse.builder()
														   .index(page.getPage())
														   .limit(page.getLimit())
														   .totalPages(userGroupPage.getTotalPages())
														   .totalElements(userGroupPage.getTotalElements())
														   .build())
						   .filter(filter)
						   .build();
	}

	private Specification<UserGroup> filterUserGroups(UserGroupFilter filter) {
		Specification<UserGroup> spec = UserGroupSpecs.notIncludeAdmin();
		if (filter.getName() != null) {
			spec = spec.and(UserGroupSpecs.hasName(filter.getName()));
		}
		return spec;
	}

	@Transactional
	public UserGroupResponse createUserGroup(CreateUserGroupRequest request) {
		UserGroup userGroup = mapToEntity(request);
		List<FeatureResponse> features = getFeatureExcludeAdminResponses(request.getFeatures());

		UserGroup savedUserGroup = userGroupRepository.save(userGroup);

		List<UserGroupFeature> UserGroupFeatures = getUserGroupFeatures(userGroup, features);
		UserGroupFeatureRepository.saveAll(UserGroupFeatures);

		return mapToDTO(features, savedUserGroup);
	}

	private List<UserGroupFeature> getUserGroupFeatures(UserGroup userGroup, List<FeatureResponse> features) {
		List<UserGroupFeature> res = new ArrayList<>();
		for (FeatureResponse featureResponse : features) {
			if (!featureResponse.isHas()) {
				continue;
			}
			Feature feature = Feature.builder()
									 .id(featureResponse.getId())
									 .code(featureResponse.getCode())
									 .name(featureResponse.getName())
									 .build();
			res.add(UserGroupFeature.builder().feature(feature).userGroup(userGroup).build());
		}
		return res;
	}

	@Transactional
	public SimpleUserGroupResponse updateUserGroup(Long userGroupId, UpdateUserGroupRequest request) {
		UserGroup userGroup = Common.findUserGroupById(userGroupId, userGroupRepository);
		if (userGroup.getName().equals(ApplicationConst.ADMIN_USER_GROUP_NAME)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.UserGroup.ADMIN_USER_GROUP_CAN_NOT_BE_INCLUDED);
		}
		Common.updateIfNotNull(request.getName(), userGroup::setName);

		UserGroup savedUserGroup = userGroupRepository.save(userGroup);
		if (request.getFeatures() != null) {
			List<FeatureResponse> features = getFeatureExcludeAdminResponses(request.getFeatures());

			List<UserGroupFeature> UserGroupFeatures = getUserGroupFeatures(savedUserGroup, features);
			Set<UserGroupFeature> existingUserGroupFeatures = savedUserGroup.getUserGroupFeatures();
			existingUserGroupFeatures.clear();

			existingUserGroupFeatures.addAll(UserGroupFeatures);
			userGroupRepository.save(savedUserGroup);
		}
		return mapToSimpleDTO(savedUserGroup);
	}

	@Transactional
	public SimpleResponse deleteUserGroup(Long userGroupId) {
		UserGroup userGroup = Common.findUserGroupById(userGroupId, userGroupRepository);
		if (userGroup.getName().equals(ApplicationConst.ADMIN_USER_GROUP_NAME)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.UserGroup.ADMIN_USER_GROUP_CAN_NOT_BE_INCLUDED);
		}

		userAuthRepository.findFirstByUserGroupId(userGroupId)
						  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															  Message.UserGroup.USER_GROUP_STILL_HAVE_STAFFS_CAN_NOT_DELETE));

		userGroupRepository.delete(userGroup);

		return new SimpleResponse();
	}

	private SimpleUserGroupResponse mapToSimpleDTO(UserGroup userGroup) {
		return SimpleUserGroupResponse.builder()
									  .id(userGroup.getId())
									  .name(userGroup.getName())
									  .build();
	}

	private UserGroup mapToEntity(CreateUserGroupRequest request) {
		return UserGroup.builder().name(request.getName()).build();
	}

	private UserGroupResponse mapToDTO(List<FeatureResponse> featureResponses, UserGroup userGroup) {
		return UserGroupResponse.builder()
								.id(userGroup.getId())
								.name(userGroup.getName())
								.features(featureResponses)
								.build();
	}

	private List<FeatureResponse> getFeatureExcludeAdminResponses(List<Long> featureIds) {
		return Common.getFeatureResponse(featureIds, false, featureRepository);
	}
}
