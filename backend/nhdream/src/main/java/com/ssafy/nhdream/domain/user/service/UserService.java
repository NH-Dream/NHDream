package com.ssafy.nhdream.domain.user.service;

import com.ssafy.nhdream.domain.user.dto.JoinReqDto;

public interface UserService {
    // 회원가입
    public int join(JoinReqDto joinReqDto);
}
