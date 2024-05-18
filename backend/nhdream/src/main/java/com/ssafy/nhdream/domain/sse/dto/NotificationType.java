package com.ssafy.nhdream.domain.sse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    SSE_CONNECT("connect", "SSE 연결성공"),
    TEST_MSG("test", "테스트 메시지"),
    DREAM_EVENT("dream", "sse 메세지 전송완료"),
    LOAN_EVENT("product", "상품 sse 메세지 전송완료")
    ;


    private final String event;
    private final String message;
}