package com.ssafy.nhdream.domain.redeposit.service;

import com.ssafy.nhdream.domain.redeposit.dto.RedepositProductCreationReqDto;
import com.ssafy.nhdream.domain.redeposit.dto.RedepositProductListDto;

import java.util.List;

public interface RedepositService {

    // 정기예금 상품 생성
    int createRedepositProduct(RedepositProductCreationReqDto redepositProductCreationReqDto);

    // 상품 목록 조회
    List<RedepositProductListDto> getRedepositProductList();
}
