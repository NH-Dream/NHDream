package com.ssafy.nhdream.domain.admin.service;

import com.ssafy.nhdream.domain.admin.dto.TrainingReviewReqDto;

public interface AdminService {

    // 교육 인증 심사
    void reviewTraning(TrainingReviewReqDto trainingReviewReqDto);
}
