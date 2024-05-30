package com.ssafy.nhdream.domain.sse.service;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.dto.SseDto;
import com.ssafy.nhdream.domain.sse.repository.SseRepository;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 240;
    private static final Long REDIRECT_TIME = 5L * 1000;

    private final SseRepository sseRepository;

    @Override
    public SseEmitter subscribe(int id, String lastEventId) {
        log.info("subscribe 실행");
        String userId = Integer.toString(id);

        String emitterId = makeTimeIncludeId(userId);
        log.info("emitterId 생성 : {}", emitterId);
        SseEmitter emitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info("emitter 생성");

        // emitter의 상태를 체크함, 완료되었는지 타임아웃이 났는지
        checkEmitterStatus(emitter, emitterId);
        log.info("emmiter 상태 체크");

        // 503 에러 방지 더미 이벤트 전송
        SseDto sseDto = SseDto.of(NotificationType.SSE_CONNECT,
                Integer.valueOf(userId), LocalDateTime.now());
        sendSse(emitter, emitterId, emitterId, sseDto);

        // 클라이언트가 미수신한 Event 전송
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        log.info("subscribe 마무리");
        return emitter;
    }

    @Override
    public SseDto sendTest(int userId) {

        SseDto sseDto = SseDto.builder()
                .trade(0)
                .state(0)
                .type("testType")
                .amount(BigDecimal.valueOf(0))
                .event("testEvent")
                .message("testMessage")
                .time(LocalDateTime.now())
                .userId(userId)
                .build();
        send(sseDto);

        return sseDto;
    }

    //특정 유저에게 알림 전송
    @Override
    public void send(SseDto sseDto) {
        log.info("send 실행");

//        int userId = sseDto.getUserId();
//
//        // 해당 유저의 SseEmitter 가져오기
//        SseEmitter emitter = sseRepository.findEmitterByUserId(String.valueOf(userId));
//        log.info("emitter는 {} 이거야", emitter);
//
//        // 유저가 없으면 종료
//        if (emitter == null) {
//            log.warn("유저가 존재하지 않습니다.");
//            return;
//        }
//
//        // 데이터 전송
//        sendSse(emitter, makeTimeIncludeId(String.valueOf(receiverId)), emitter.toString(), sseDto);
//
//        sendSseEvent(emitter, sseDto);



        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = sseRepository.findAllEmitterStartWithByUserId(
                String.valueOf(sseDto.getUserId()));

        log.info("send의 sseEmitter 가져오기 : {}", sseEmitters);


        sseEmitters.forEach(
                (key, emitter) -> {
                    log.info("sseEmitter : {}", key);
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    sseRepository.saveEventCache(key, sseDto);

                    log.info("sendSseDto로 보내기");

                    // 데이터 전송
                    sendSse(emitter, makeTimeIncludeId(String.valueOf(sseDto.getUserId())),
                            key, sseDto);
                }
        );
    }

    private void sendLostData(String lastEventId, String userId, String emitterId,
                              SseEmitter emitter) {

        Map<String, SseDto> eventCaches = sseRepository.findAllEventCacheStartWithByUserId(
                userId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(
                        entry -> sendSse(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // 종료 상태
    private void checkEmitterStatus(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> {
            sseRepository.deleteById(emitterId);
            //redisMessageListenerContainer.removeMessageListener(messageListener);
        });
        emitter.onTimeout(() -> {
            sseRepository.deleteById(emitterId);
            //redisMessageListenerContainer.removeMessageListener(messageListener);
        });
        emitter.onError((e) -> sseRepository.deleteById(emitterId));
    }

    private void sendSse(SseEmitter emitter, String eventId, String emitterId,
                         SseDto dto) {
        log.info("sendSse 실행");

        int underscoreEvent = eventId.indexOf('_');

        // '_' 이전의 부분 문자열 추출
        String beforeUnderscoreEvent = eventId.substring(0, underscoreEvent);

        // 추출한 부분 문자열을 정수로 변환
        int eventValue = Integer.parseInt(beforeUnderscoreEvent);

        int underscoreEmitter = emitterId.indexOf('_');

        // '_' 이전의 부분 문자열 추출
        String beforeUnderscoreEmitter = emitterId.substring(0, underscoreEmitter);

        // 추출한 부분 문자열을 정수로 변환
        int emitterValue = Integer.parseInt(beforeUnderscoreEmitter);

        log.info("event : {}, emitter : {}", eventValue, emitterValue);

        // 만약 eventId의 유저값이 보내는 유저값과 같다면
        if (eventValue == emitterValue) {
            log.info("이벤트 아이디 값과 에미터 아이디 값이 같음 eventId : {}, emitterId : {}", eventId, emitterId);


            try {
                emitter.send(SseEmitter.event()
                        .id(eventId)
                        .reconnectTime(REDIRECT_TIME)
                        .name(dto.getEvent())
                        .data(dto));
                log.info("eventId : {}, emitterId : {}, SseDto : {}", eventId, emitterId, dto);
                log.info("{}", dto.getEvent());
                log.info("send 완료~");
            } catch (IOException exception) {
                log.info("sendSse 안됐어;;;;");
                sseRepository.deleteById(emitterId);
                emitter.completeWithError(exception);
            }
        }
    }


    // SseEmitter에 이벤트를 보내는 메서드
    private void sendSseEvent(SseEmitter emitter, SseDto event) {
        log.info("제발 해줘");

        try {
            emitter.send(emitter.event()
                    .name(event.getEvent())
                    .data(event));
        } catch (IOException e) {
            // 예외 처리
            throw new CustomException(ExceptionType.USER_NOT_EXIST);
        }
    }


    private String makeTimeIncludeId(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }



}
