package com.ssafy.nhdream.common.auth;


import com.ssafy.nhdream.common.jwt.JWTUtil;
import com.ssafy.nhdream.entity.user.User;
import com.ssafy.nhdream.entity.user.UserType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("Enter JWTFilter");

        // Header 에서 access 토큰 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if(accessToken == null){
            log.info("토큰 없어서 다음 필터로 넘어감");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try{
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e){
            String jsonResponse = "{\"dataHeader\": {\"successCode\": 1, \"resultCode\": \"O-003\", \"resultMessage\": \"만료된 토큰입니다.\"}, \"dataBody\": null}";

            // JSON 형식으로 응답을 반환하기 위해 Content-Type 설정
            response.setContentType("application/json");
            // 상태 코드 설정
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            // JSON 응답을 출력
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
            out.print(jsonResponse);
            out.flush();

            return;
        }

        // 토큰이 access 인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if(!category.equals("access")){
            String jsonResponse = "{\"dataHeader\": {\"successCode\": 1, \"resultCode\": \"U-002\", \"resultMessage\": \"유효하지 않은 토큰입니다.\"}, \"dataBody\": null}";

            // JSON 형식으로 응답을 반환하기 위해 Content-Type 설정
            response.setContentType("application/json");
            // 상태 코드 설정
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            // JSON 응답을 출력
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
            out.print(jsonResponse);
            out.flush();

            return;
        }

        String loginId = jwtUtil.getLoginId(accessToken);
        String name = jwtUtil.getName(accessToken);
        int userID = jwtUtil.getUserId(accessToken);
        String userType = jwtUtil.getUserType(accessToken);

        User user = User.builder()
                .id(userID)
                .loginId(loginId)
                .name(name)
                .type(UserType.valueOf(userType))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
