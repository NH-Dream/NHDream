package com.ssafy.nhdream.common.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    SseEmitter subscribe (String userId, String lastEventId);
    void sendTest(String userId);

}
