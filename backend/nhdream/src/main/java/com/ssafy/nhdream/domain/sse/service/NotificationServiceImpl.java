package com.ssafy.nhdream.domain.sse.service;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.dto.SseDto;
import com.ssafy.nhdream.domain.sse.entity.Notification;
import com.ssafy.nhdream.domain.sse.repository.NotificationRepository;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SseService sseService;

    @Override
    @Transactional
    public Notification saveNotice(NotificationType notificationType, int userId, BigDecimal amount, int trade, int state, String name) {
        // 정보 받아서 notification 생성 후 sse를 통해 보내는 로직
        // 자유 입출금 때는 상대방에게 보내고 (왜냐면 무조건 내 기준으로는 출금이니깐)
        // 대출 예금 적금 시에는 나한테 보내기

        log.info("notification saveNotice 실행 : {}", notificationType);
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        Notification notification = Notification.builder()
                .trade(trade)
                .notificationType(notificationType)
                .amount(amount)
                .state(state)
                .name(name)
                .user(user)
                .build();

        notificationRepository.save(notification);

        log.info("saveNotification의 sseDto 생성");
        SseDto sseDto = SseDto.builder()
                .trade(trade)
                .notificationId(notification.getId())
                .userId(userId)
                .state(state)
                .amount(amount)
                .time(notification.getCreatedAt())
                .name(name)
                .event(notification.getNotificationType().getEvent())
                .message(notification.getNotificationType().getMessage())
                .type(notification.getNotificationType().name())
                .build();

        log.info("SseDto 생성 후 보내기");

        sseService.send(sseDto);

        return notification;
    }

    @Override
    public void notificationTest (int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        log.info("notiTest 실행");
        saveNotice(NotificationType.TEST_MSG, id, BigDecimal.valueOf(0),1, 2, "testName");

    }


//    @Override
//    public List<SseDto> getNoticeList(int id) {
//        List<Notification> notifications = notificationRepository.getNoticeAllByUserId(id);
//        List<SseDto> sseDtos = new ArrayList<>();
//
//        for (Notification notification : notifications) {
//            SseDto sseDto = SseDto.builder()
//                    .notificationId(notification.getId())
//                    .trade(notification.getTrade())
//                    .name(notification.getName())
//                    .amount(notification.getAmount())
//                    .state(notification.getState())
//                    .userId(notification.getUser().getId())
//                    .build();
//
//            sseDtos.add(sseDto);
//        }
//
//        return sseDtos;
//    }

//    @Override
//    @Transactional
//    public void readNotice(Long id, UserDetails loginUser) {
//
//    }
}
