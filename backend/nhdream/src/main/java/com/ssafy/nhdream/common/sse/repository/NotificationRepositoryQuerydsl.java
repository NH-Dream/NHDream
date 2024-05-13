package com.ssafy.nhdream.common.sse.repository;

import com.ssafy.nhdream.common.sse.dto.SseDto;

import java.util.List;

public interface NotificationRepositoryQuerydsl {

    List<SseDto> getNoticeAllByUserId(int userId);

}
