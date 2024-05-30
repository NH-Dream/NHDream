package com.ssafy.nhdream.common.jwt;

import com.ssafy.nhdream.entity.user.UserType;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    private JWTUtil(@Value("${spring.jwt.secret}") String secret){
        // yaml 파일에 등록한 키를 기반으로 객체키(SecretKey) 생성
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 검증 메서드 - 추출 메서드
    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getLoginId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId", String.class);
    }

    public String getName(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("name", String.class);
    }

    public Integer getUserId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Integer.class);
    }

    public String getUserType(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userType", String.class);
    }

    public String getWalletAddress(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("walletAddress", String.class);
    }

    public String getPhone(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("phone", String.class);
    }

    // 검증 - 만료 기간 추출 메서드
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 토큰 생성 메서드
    public String createJwt(String category, int userId, String loginId, String name, String userType, String walletAddress, String phone ,Long expiredMs){
        return Jwts.builder()
                .claim("category", category)        // 토큰 종류(Access, Refresh)
                .claim("userId", userId)            // 회원 PK
                .claim("loginId", loginId)          // 로그인 아이디
                .claim("name", name)                // 이름
                .claim("userType", userType)        // 유저 직업
                .claim("walletAddress", walletAddress)    // 유저 직업
                .claim("phone", phone)              // 유저 전화번호
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}
