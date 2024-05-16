package com.ssafy.nhdream.domain.sse.repository;

import com.ssafy.nhdream.domain.sse.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

//    List<Notification> findAllByReceiverIdOrderByTimeDesc(int userId);
//
//    Integer countByReceiverIdAndReadStatusIsFalse(Long receiverId);

}
