package com.ssafy.nhdream.common.sse.service;

import com.ssafy.nhdream.common.sse.dto.SseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    SseEmitter subscribe (String userId, String lastEventId);
    void sendTest(String userId);
    void send (SseDto sseDto);
}
