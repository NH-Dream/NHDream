package com.ssafy.nhdream.common.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@RedisHash(value = "email_auth", timeToLive = 60*60*24*3)
public class EmailAuth {

    // 식별ID
    @Id
    private String email;

    // 인증번호
    private String randomNum;

    @Builder
    public EmailAuth(String email, String randomNum) {
        this.email = email;
        this.randomNum = randomNum;
    }
}

