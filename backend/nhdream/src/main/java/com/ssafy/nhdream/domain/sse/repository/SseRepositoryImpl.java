package com.ssafy.nhdream.domain.sse.repository;

import com.ssafy.nhdream.domain.sse.dto.SseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SseRepositoryImpl implements SseRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, SseDto> eventCache = new ConcurrentHashMap<>();

    // emitter를 저장
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {

        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    // 이벤트를 저장
    @Override
    public void saveEventCache(String eventId, SseDto event) {

        eventCache.put(eventId, event);

    }

    @Override
    public Map<String, SseEmitter> findAllEmitter() {
        return emitters;
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public SseEmitter findEmitterByUserId(String userId) {
        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            if (entry.getKey().startsWith(userId)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Map<String, SseDto> findAllEventCacheStartWithByUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithUserId(String userId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(userId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithId(String userId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(userId)) {
                        emitters.remove(key);
                    }
                }
        );

    }
}
