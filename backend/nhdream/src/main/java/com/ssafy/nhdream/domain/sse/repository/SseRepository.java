package com.ssafy.nhdream.domain.sse.repository;


import com.ssafy.nhdream.domain.sse.dto.SseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventId, SseDto event);

    Map<String, SseEmitter> findAllEmitter();

    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);

    public SseEmitter findEmitterByUserId(String userId);

    Map<String, SseDto> findAllEventCacheStartWithByUserId(String userId);

    void deleteById(String id);

    void deleteAllEmitterStartWithUserId(String userId);

    void deleteAllEventCacheStartWithId(String userId);

}
