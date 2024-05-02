package com.ssafy.nhdream.domain.saving.service;

import com.ssafy.nhdream.domain.saving.dto.SavingProductListDto;

import java.util.List;

public interface SavingService {

    //적금 상품리스트 조회
    List<SavingProductListDto> getProductList();
}
