package com.ssafy.nhdream.common.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {

    // Refresh 값을 활용한 조회
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    // Refresh 값을 활용한 삭제
    void deleteById(String loginId);

}

