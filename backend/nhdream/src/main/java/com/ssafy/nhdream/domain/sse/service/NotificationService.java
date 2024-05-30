package com.ssafy.nhdream.domain.sse.service;

import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.entity.Notification;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;

public interface NotificationService {

    Notification saveNotice(NotificationType notificationType, int userId, BigDecimal amount, int trade, int state, String name);

//    List<SseDto> getNoticeList(int id);

//    void readNotice(Long id, UserDetails loginUser);

    void notificationTest (int id);

}
