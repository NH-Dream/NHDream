package com.ssafy.nhdream.domain.admin.service;

import com.ssafy.nhdream.domain.admin.dto.ReviewReqDto;

public interface AdminService {

    // 교육 인증 심사
    void reviewTraning(ReviewReqDto reviewReqDto);

    // 농부 인증 심사
    void reviewFarmer(ReviewReqDto reviewReqDto);
}
