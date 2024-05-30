package com.ssafy.nhdream.domain.user.service;
import com.ssafy.nhdream.domain.user.dto.VerifyCodeReqDto;
import com.ssafy.nhdream.domain.user.dto.VerifyEmailReqDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

public interface MailService {
    // 인증 코드 생성
    public String createAuthCode();

    // 메일 내용 생성
    public MimeMessage createMessage(String receiver, String authCode) throws MessagingException, UnsupportedEncodingException;

    // 회원가입을 위한 메일 전송
    public void sendJoinMessage(String email) throws MessagingException, UnsupportedEncodingException;

    // 인증 코드 검증
    void verifyAuthCode(VerifyCodeReqDto verifyCodeReqDto);
}
