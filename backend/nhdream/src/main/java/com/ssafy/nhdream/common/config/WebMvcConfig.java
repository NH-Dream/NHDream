package com.ssafy.nhdream.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        List<String> origins = List.of("https://k10s209.p.ssafy.io", "http://localhost:5173", "http://k10s209.p.ssafy.io:1322");

        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(String.join(",", origins)) // 허용할 오리진 지정
                .allowedMethods("*") // 메서드
                .allowCredentials(true); // 쿠키를 포함한 요청 허용
    }
}

