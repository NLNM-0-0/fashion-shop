package com.fashion.backend.entity.IdClass;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserGroupFeatureId implements Serializable {
	private Long userGroup;
	private Long feature;
}
