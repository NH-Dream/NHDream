package com.ssafy.nhdream.common.config;



import com.ssafy.nhdream.common.auth.CustomLogoutFilter;
import com.ssafy.nhdream.common.auth.JWTFilter;
import com.ssafy.nhdream.common.auth.LoginFilter;
import com.ssafy.nhdream.common.jwt.JWTUtil;
import com.ssafy.nhdream.common.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PERMIT_URL_ARRAY = {
            /* api */
            "/api/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            /* image */
            "/image/**",
            /* 회원가입 */
            "/user",
            /* 로그인 */
            "/user/login/**",
            /* 비밀번호 찾기 */
            "user/findPassword/**",
            /* 토큰 재발급 */
            "/refresh/reissue"
    };

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // 불필요한 특성 비활성화
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션 설정
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로별 인가 작업
        http.authorizeHttpRequests(auth -> auth
//                        .requestMatchers(PERMIT_URL_ARRAY).permitAll()
//                        .anyRequest().authenticated())
                        .anyRequest().permitAll());

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        List<String> origins = List.of("https://k10s209.p.ssafy.io", "http://localhost:5173", "http://k10s209.p.ssafy.io:1322");

        // CORS 설정
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOrigins(origins);
            corsConfig.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.addExposedHeader("access");
            corsConfig.setAllowCredentials(true);

            return corsConfig;
        }));

        return http.build();
    }

}

