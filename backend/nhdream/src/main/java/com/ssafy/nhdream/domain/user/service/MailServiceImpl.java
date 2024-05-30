package com.ssafy.nhdream.domain.user.service;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.redis.EmailAuth;
import com.ssafy.nhdream.common.redis.MailRepository;
import com.ssafy.nhdream.domain.user.dto.VerifyCodeReqDto;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String email;


    @Override
    public String createAuthCode() {
        // 인증 코드 저장 버퍼
        StringBuffer sb = new StringBuffer();

        // 랜덤 객체 생성
        Random random = new Random();

        // 인증코드 6자리 생성
        for(int i=0; i<6; i++)
            sb.append(random.nextInt(10));

        return sb.toString();
    }

    @Override
    public MimeMessage createMessage(String receiver, String authCode) throws MessagingException, UnsupportedEncodingException {
        // 메일 내용 객체 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        // 메일 제목
        message.setSubject("NHDream 인증 코드 안내");

        // 받는 사람 지정
        message.addRecipients(MimeMessage.RecipientType.TO, receiver);

        // 메일 내용 (SubType HTML 지정)
        String msg =
                "<h1 style=\"font-size: 30px; padding: 15px 10px;\">NHDream 인증 코드 안내드립니다.</h1>"+
                        "<p style=\"font-size: 17px; padding: 10px 10px;\">" +
                        "아래 인증 코드를 입력해주세요.</p>"+
                        "<p style=\"font-size: 17px; padding: 10px 10px;\">" +
                        "저희 NHDream 이용해주셔서 감사합니다.</p>"+
                        "<p style=\"font-size: 40px; font-weight: bold; padding: 10px 10px;\">" +
                        authCode+"</p>";

        // 메일 내용 설정 (내용, Charset, SubType)
        message.setText(msg, "utf-8", "html");

        // 발신자 정보 설정 (발신자, 보내는 사람 이름)
        // 예) 보내는 사람 이름 <mefi_undefined@naver.com>
        message.setFrom(new InternetAddress(email,"FarmYo"));

        return message;
    }

    @Transactional
    @Override
    public void sendJoinMessage(String email) throws MessagingException, UnsupportedEncodingException {
        // 이메일 중복 검사
        if(userRepository.findByEmail(email).isPresent())
            throw new CustomException(ExceptionType.EMAIL_EXIST);

        // 인증 코드 생성
        String authCode = createAuthCode();

        // 메일 내용 객체 생성
        MimeMessage message = createMessage(email, authCode);

        // 엔티티 생성
        EmailAuth emailAuth = EmailAuth.builder()
                .email(email)
                .randomNum(authCode)
                .build();

        // 이메일 저장
        mailRepository.save(emailAuth);

        // 메일 전송
        javaMailSender.send(message);
    }

    @Override
    public void verifyAuthCode(VerifyCodeReqDto verifyCodeReqDto) {
        // 인증 코드 유효성 검사

        // 이메일로 인증 엔티티 조회
        EmailAuth emailAuth = mailRepository.findById(verifyCodeReqDto.getEmail()).orElseThrow(() -> new CustomException(ExceptionType.CODE_TIME_EXPIRED));

        // 인증 코드 일치 여부
        if(!emailAuth.getRandomNum().equals(verifyCodeReqDto.getAuthCode())) throw new CustomException(ExceptionType.CODE_NOT_MATCH);
    }
}
