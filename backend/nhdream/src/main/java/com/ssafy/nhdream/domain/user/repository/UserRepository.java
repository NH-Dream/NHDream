package com.ssafy.nhdream.domain.user.repository;

import com.ssafy.nhdream.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
