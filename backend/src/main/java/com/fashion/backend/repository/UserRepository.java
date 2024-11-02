package com.fashion.backend.repository;

import com.fashion.backend.entity.User;
import com.fashion.backend.payload.customer.CustomerSpecs;
import com.fashion.backend.payload.staff.StaffSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	@Query("SELECT u FROM User u WHERE u.userAuth.isDeleted <> true")
	List<User> findAllNotDeleted();

	@Query("SELECT u FROM User u WHERE u.id <> :excludedId AND u.id IN :ids AND u.userAuth.phone = null AND u.userAuth.isDeleted <> true")
	List<User> findByIdInAndAndIdNotEqualAndNotHasPhoneAndNotDelete(List<Long> ids, Long excludedId);

	Optional<User> findFirstByUserAuthId(Long userAuthId);

	default Page<User> findAllNotHasPhoneAndNotDeleteAndNotHaveEmail(String email,
																	 Specification<User> specs,
																	 Pageable pageable) {
		Specification<User> spec = StaffSpecs.isNotDeleted()
											 .and(StaffSpecs.isStaff())
											 .and(StaffSpecs.notHasEmail(email));
		return findAll(specs.and(spec), pageable);
	}

	default Page<User> findAllNotHasPhone(Specification<User> specs, Pageable pageable) {
		Specification<User> spec = StaffSpecs.isStaff();
		return findAll(specs.and(spec), pageable);
	}

	default Page<User> findAllHasPhoneAndNotDelete(Specification<User> specs, Pageable pageable) {
		Specification<User> spec = CustomerSpecs.isNotDeleted()
												.and(CustomerSpecs.isNormalUser());
		return findAll(specs.and(spec), pageable);
	}

	default Page<User> findAllHasPhone(Specification<User> specs, Pageable pageable) {
		Specification<User> spec = CustomerSpecs.isNormalUser();
		return findAll(specs.and(spec), pageable);
	}
}
