package com.ssafy.nhdream.domain.sse.controller;

import com.ssafy.nhdream.common.auth.CustomUserDetails;
import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.sse.dto.SseDto;
import com.ssafy.nhdream.domain.sse.service.NotificationService;
import com.ssafy.nhdream.domain.sse.service.SseService;
import com.ssafy.nhdream.entity.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "8. SSE", description = "SSE API")
public class SseController {

    private final SseService sseService;
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE 연결", description = "SSE 연결을 위한 API")
    public ResponseEntity<SseEmitter> subscribe(
//            Authentication authentication,
            @RequestParam(name = "userId")
            int id,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
            ) {

//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPri
//        ncipal();

        SseEmitter emitter = sseService.subscribe(id, lastEventId);
//        SseEmitter emitter = sseService.subscribe(userDetails.getId(), lastEventId);

        log.info("이제 프론트로 보낸다?? : {}", emitter);
        return ResponseEntity.ok(emitter);
    }

    @Operation(summary = "sse Test")
    @PostMapping("/send/{userId}")
    public ResponseEntity<? extends BaseResponseBody> sendTest(
            @PathVariable(name = "userId")
            int userId
    ) {
        SseDto sseDto = sseService.sendTest(userId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, sseDto));
    }

    @Operation(summary = "notiTest")
    @PostMapping("/notiTest")
    public ResponseEntity<? extends BaseResponseBody> sendNotificationTest (
            @RequestParam(name = "userId")
            int id
    ) {
        notificationService.notificationTest(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, 0)
        );
    }

}
