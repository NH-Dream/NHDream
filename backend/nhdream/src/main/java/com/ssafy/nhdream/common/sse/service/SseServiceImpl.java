package com.ssafy.nhdream.common.sse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseServiceImpl implements SseService {

    @Override
    public SseEmitter subscribe(String userId, String lastEventId) {
        return null;
    }

    @Override
    public void sendTest(String userId) {

    }

}
