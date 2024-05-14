package com.ssafy.nhdream.common.sse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    SSE_CONNECT("connect", "SSE 연결성공"),
    TEST_MSG("test", "테스트 메시지"),
    FOLLOW_EVENT("follow", "팔로우 합니다."),
    KOCK_ACTION("kock", "콕 찔렸습니다");

    private final String event;
    private final String message;
}