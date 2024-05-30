package com.ssafy.nhdream.common.auth;

import com.ssafy.nhdream.common.jwt.JWTUtil;
import com.ssafy.nhdream.common.redis.RefreshToken;
import com.ssafy.nhdream.common.redis.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshRepository;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/api\\/users\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        //refresh null check
        if (refresh == null) {
            String jsonResponse = "{\"dataHeader\": {\"successCode\": 1, \"resultCode\": \"O-001\", \"resultMessage\": \"토큰이 존재하지 않습니다.\"}, \"dataBody\": null}";

            // JSON 형식으로 응답을 반환하기 위해 Content-Type 설정
            response.setContentType("application/json");
            // 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            // JSON 응답을 출력
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
            out.print(jsonResponse);
            out.flush();

            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 이미 로그아웃 완료 된 상태
            //response status code
            String jsonResponse = "{\"dataHeader\": {\"successCode\": 1, \"resultCode\": \"O-002\", \"resultMessage\": \"토큰이 유효기간이 지났습니다.\"}, \"dataBody\": null}";

            // JSON 형식으로 응답을 반환하기 위해 Content-Type 설정
            response.setContentType("application/json");
            // 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            // JSON 응답을 출력
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
            out.print(jsonResponse);
            out.flush();
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            String jsonResponse = "{\"dataHeader\": {\"successCode\": 1, \"resultCode\": \"O-003\", \"resultMessage\": \"유효하지 않은 토큰입니다.\"}, \"dataBody\": null}";

            // JSON 형식으로 응답을 반환하기 위해 Content-Type 설정
            response.setContentType("application/json");
            // 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            // JSON 응답을 출력
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
            out.print(jsonResponse);
            out.flush();

            return;
        }

        // 로그아웃 진행
        // DB에 저장되어 있는지 확인
        Optional<RefreshToken> refreshToken = refreshRepository.findByRefreshToken(refresh);

        if (refreshToken.isPresent()) {
            RefreshToken reToken = refreshToken.get();
            log.info("{}", reToken.getLoginId());
            log.info("{}", reToken.getAccessToken());
            log.info("{}", reToken.getRefreshToken());

            //Refresh 토큰 DB에서 제거
            refreshRepository.deleteById(reToken.getLoginId());
        }

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
