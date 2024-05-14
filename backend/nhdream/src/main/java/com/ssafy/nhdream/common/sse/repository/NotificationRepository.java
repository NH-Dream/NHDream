package com.ssafy.nhdream.common.sse.repository;

import com.ssafy.nhdream.common.sse.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

//    List<Notification> findAllByReceiverIdOrderByTimeDesc(int userId);
//
//    Integer countByReceiverIdAndReadStatusIsFalse(Long receiverId);

}
