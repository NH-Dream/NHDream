package com.ssafy.nhdream.domain.user.repository;

import com.ssafy.nhdream.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 로그인 아이디로 회원 조회
    Optional<User> findByLoginId(String loginId);

}
