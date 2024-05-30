package com.ssafy.nhdream.domain.saving.service;

import com.ssafy.nhdream.domain.saving.dto.*;

import java.util.List;

public interface SavingService {

    //적금 상품리스트 조회
    List<SavingProductListResDto> getSavingProductList();

    //적금상품생성
    int createSavingProduct(SavingProductCreationReqDto savingProductCreationReqDto);

    //적금상품 상세조회
    SavingProductDetailResDto getSavingProductDetail(int savingProductId);

    //적금계좌생성
    int createSavingAccount(SavingAccountCreationReqDto savingAccountCreationReqDto, int userId);

    //적금거래내역 조회
    SavingHistoryResDto getSavingHistory(int savingAccountId, int userId, int page, int size);

    //적금계좌상세조회
    SavingAccountDetailResDto getSavingAccountDetail(int savingAccountId, int userId);

    //적금만기해지
    void expireSavingAccount();

    //적금이자넣기
    void interestToSavingAccount();
}
