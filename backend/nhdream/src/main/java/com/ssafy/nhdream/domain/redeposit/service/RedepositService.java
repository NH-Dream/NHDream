package com.ssafy.nhdream.domain.redeposit.service;

import com.ssafy.nhdream.domain.redeposit.dto.RedepositProductCreationReqDto;

public interface RedepositService {

    // 정기예금 상품 생성
    int createRedepositProduct(RedepositProductCreationReqDto redepositProductCreationReqDto);
}
