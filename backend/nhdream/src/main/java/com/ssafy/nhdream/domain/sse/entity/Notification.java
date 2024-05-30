package com.ssafy.nhdream.domain.sse.entity;


import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "notification_name")
    private String name;

    @Column(name = "notification_trade")
    private Integer trade;

    @Column(name = "notification_amount")
    private BigDecimal amount;

    @Column(name = "notification_state")
    private Integer state;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Builder
    public Notification (User user, String name, int trade, BigDecimal amount, int state,
        NotificationType notificationType) {
        this.user = user;
        this.name = name;
        this.trade = trade;
        this.amount = amount;
        this.state = state;
        this.notificationType = notificationType;
    }


//    private Boolean readStatus;
//
//    @Enumerated(value = EnumType.STRING)
//    private NotificationType notificationType;
//
//    private Integer receiverId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User sender;
//
//    private LocalDateTime time;
//
//    public void updateReadStatus(Boolean readStatus) {
//        this.readStatus = readStatus;
//    }
//
//    public static Notification of(NotificationType notificationType, int receiverId, User sender,
//                                  LocalDateTime time) {
//        return builder()
//                .readStatus(false)
//                .notificationType(notificationType)
//                .receiverId(receiverId)
//                .sender(sender)
//                .time(time)
//                .build();
//    }

}
