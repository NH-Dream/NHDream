package com.ssafy.nhdream.domain.user.openApi;


import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
public class OpenApiManager {

    @Value("${spring.openAPI.serviceKey}")
    private String serviceKey;
    private final String BASE_URL = "http://api.odcloud.kr/api/nts-businessman/v1/validate";

    public void validateLicense(String licenseNum, String representative, String openDate) {

        // URL 만들기
        String url = BASE_URL +
                "?serviceKey=" + serviceKey;

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        // 요청 바디 생성
        String requestBody = "{\n" +
                "  \"businesses\": [\n" +
                "    {\n" +
                "      \"b_no\": \"" + licenseNum + "\",\n" +
                "      \"start_dt\": \"" + openDate + "\",\n" +
                "      \"p_nm\": \"" + representative + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 헤더 & 바디를 삽입한 요청 객체 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        URI uri = null;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new CustomException(ExceptionType.INTERNAL_SERVER_ERROR);
        }

        // 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        // 응답 바디 받기
        String result = responseEntity.getBody();

        // 만약 유효하지 않다면 예외 처리
        if(result.contains("\"valid\":\"02\"")) throw new CustomException(ExceptionType.INVALID_BUSINESS_LICENSE);
    }

}
