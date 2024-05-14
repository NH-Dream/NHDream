package com.ssafy.nhdream.common.sse.repository;

import com.ssafy.nhdream.common.sse.dto.SseDto;
import com.ssafy.nhdream.common.sse.entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositoryQuerydsl {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
    List<Notification> getNoticeAllByUserId(int userId);

}
