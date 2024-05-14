package com.ssafy.nhdream.common.sse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long id;
    private Boolean readStatus;
    private NotificationType notificationType;
    private Long receiverId;
    private Long senderId;
    private String senderName;
    private LocalDateTime time;
}