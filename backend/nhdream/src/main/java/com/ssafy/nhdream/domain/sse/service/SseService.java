package com.ssafy.nhdream.domain.sse.service;

import com.ssafy.nhdream.domain.sse.dto.SseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    SseEmitter subscribe (int id, String lastEventId);
    SseDto sendTest(int userId);
    void send (SseDto sseDto);
}
