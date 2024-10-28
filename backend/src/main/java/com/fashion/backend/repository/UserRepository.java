package com.fashion.backend.repository;

import com.fashion.backend.constant.ApplicationConst;
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

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	@Query("SELECT u FROM User u WHERE u.userAuth.isDeleted <> true")
	List<User> findAllNotDeleted();

	@Query("SELECT u FROM User u WHERE u.id <> :excludedId AND u.id IN :ids AND u.userAuth.phone = null AND u.userAuth.isDeleted <> true")
	List<User> findByIdInAndAndIdNotEqualAndNotHasPhoneAndNotDelete(List<Long> ids, Long excludedId);

	default Page<User> findAllNotHasPhoneAndNotDeleteAndNotAdmin(Specification<User> specs, Pageable pageable) {
		Specification<User> spec = StaffSpecs.isNotDeleted()
											 .and(StaffSpecs.isStaff())
											 .and(StaffSpecs.notHaveEmail(ApplicationConst.ADMIN_EMAIL));
		return findAll(specs.and(spec), pageable);
	}

	default Page<User> findAllHasPhoneAndNotDelete(Specification<User> specs, Pageable pageable) {
		Specification<User> spec = CustomerSpecs.isNotDeleted()
												.and(CustomerSpecs.isNormalUser());
		return findAll(specs.and(spec), pageable);
	}
}
