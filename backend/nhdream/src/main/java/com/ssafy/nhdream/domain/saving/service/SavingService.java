package com.ssafy.nhdream.domain.saving.service;

import com.ssafy.nhdream.domain.saving.dto.SavingProductCreationReqDto;
import com.ssafy.nhdream.domain.saving.dto.SavingProductListResDto;

import java.util.List;

public interface SavingService {

    //적금 상품리스트 조회
    List<SavingProductListResDto> getProductList();

    //적금상품생성
    int createSavingProduct(SavingProductCreationReqDto savingProductCreationReqDto);
}
