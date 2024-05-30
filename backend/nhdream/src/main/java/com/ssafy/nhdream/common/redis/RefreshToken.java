package com.ssafy.nhdream.common.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@ToString
@RedisHash(value = "refresh_token", timeToLive = 60*60*24*3)
public class RefreshToken {

    @Indexed
    private String refreshToken;

    @Id
    private String loginId;

    private String accessToken;

}
