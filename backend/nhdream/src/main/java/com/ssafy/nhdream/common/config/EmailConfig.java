package com.ssafy.nhdream.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    // 이메일
    @Value("${spring.mail.username}")
    private String userEmail;

    // 비밀번호
    @Value("${spring.mail.password}")
    private String password;

    // 서버 주소
    @Value("${spring.mail.host}")
    private String host;

    // 포트
    @Value("${spring.mail.port}")
    private int port;

    // 메일 정보 설정
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        // 사용 이메일 도메인 (서버 주소)
        javaMailSender.setHost(host);

        // 이메일 (아이디)
        javaMailSender.setUsername(userEmail);

        // 비밀번호
        javaMailSender.setPassword(password);

        // 사용 포트
        javaMailSender.setPort(port);

        // 메일 인증 서버 정보 가져오기
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    // 메일 정보 부가 설정
    private Properties getMailProperties() {

        Properties properties = new Properties();

        // 프로토콜 설정
        properties.setProperty("mail.transport.protocol", "smtp");

        // SMTP 인증
        properties.setProperty("mail.smtp.auth", "true");

        // SMTP Starttls 사용
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // 디버그 사용
        properties.setProperty("mail.debug", "true");

        // SSL 인증 서버 주소
        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");

        // SSL 사용
        properties.setProperty("mail.smtp.ssl.enable","true");

        return properties;
    }
}
