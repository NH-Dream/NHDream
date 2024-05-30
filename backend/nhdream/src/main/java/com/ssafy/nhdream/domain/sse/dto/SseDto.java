package com.ssafy.nhdream.domain.sse.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SseDto {

    private int notificationId;
    private int state;
    private int userId;
    private int trade;
    private String name;
    private BigDecimal amount;
    private String type;
    private String message;
    private String event;
    private LocalDateTime time;


    public static SseDto of(NotificationType notificationType, int receiverId,
                            LocalDateTime time) {
        return SseDto.builder()
                .type(notificationType.name())
                .event(notificationType.getEvent())
                .message(notificationType.getMessage())
                .userId(receiverId)
                .time(time)
                .build();
    }



    //    private int notificationId;
//    private Boolean readStatus;
//    private String type;
//    private String event;
//    private String message;
//    private int receiverId;
//    private int senderId;
//    private String senderName;
//    private String senderImg;
//    private LocalDateTime time;

//    @QueryProjection
//    public SseDto(int notificationId, Boolean readStatus, NotificationType notificationType,
//                  int receiverId, int senderId, String senderName, String senderImg, LocalDateTime time) {
//        this.notificationId = notificationId;
//        this.readStatus = readStatus;
//        this.type = notificationType.name();
//        this.event = notificationType.getEvent();
//        this.message = notificationType.getMessage();
//        this.receiverId = receiverId;
//        this.senderId = senderId;
//        this.senderName = senderName;
//        this.time = time;
//    }
//
//
//    public static SseDto from(Notification notification) {
//        int senderId = 0;
//        String senderName = null;
//        if (notification.getSender() != null) {
//            senderId = notification.getSender().getId();
//            senderName = notification.getSender().getName();
//        }
//        return builder()
//                .notificationId(notification.getId())
//                .readStatus(notification.getReadStatus())
//                .type(notification.getNotificationType().name())
//                .event(notification.getNotificationType().getEvent())
//                .message(notification.getNotificationType().getMessage())
//                .receiverId(notification.getReceiverId())
//                .senderId(senderId)
//                .senderName(senderName)
//                .time(notification.getTime())
//                .build();
//    }

}
