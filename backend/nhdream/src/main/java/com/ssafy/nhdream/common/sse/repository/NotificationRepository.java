package com.ssafy.nhdream.common.sse.repository;

import com.ssafy.nhdream.common.sse.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, NotificationRepositoryQuerydsl {

    List<Notification> findAllByReceiverIdOrderByTimeDesc(int userId);

    Integer countByReceiverIdAndReadStatusIsFalse(Long receiverId);

}
