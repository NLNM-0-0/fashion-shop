package com.fashion.backend.repository;

import com.fashion.backend.entity.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
	@Query("SELECT n FROM Notification n WHERE n.toUser.userAuth.id = :toUserId")
	List<Notification> findAllByToUserId(Long toUserId, Sort sort);

	@Query("SELECT n FROM Notification n WHERE n.toUser.userAuth.id = :toUserId")
	List<Notification> findAllByToUserId(Long toUserId);

	@Query("SELECT COUNT(n) FROM Notification n WHERE n.toUser.userAuth.id = :toUserId AND n.seen = false")
	int countUnseenNotificationsByToUserId(Long toUserId);

	@Query("SELECT n FROM Notification n WHERE n.toUser.userAuth.id = :toUserId AND n.seen = false")
	List<Notification> findAllUnseenByToUserId(Long toUserId);
}
