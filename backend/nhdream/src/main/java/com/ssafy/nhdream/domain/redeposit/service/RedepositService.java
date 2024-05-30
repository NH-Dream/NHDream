package com.ssafy.nhdream.domain.redeposit.service;

import com.ssafy.nhdream.domain.redeposit.dto.*;

import java.util.List;

public interface RedepositService {

    // 정기예금 상품 생성
    int createRedepositProduct(RedepositProductCreationReqDto redepositProductCreationReqDto);

    // 상품 목록 조회
    List<RedepositProductListDto> getRedepositProductList();

    // 상품 상세 정보
    RedepositProductDetailResDto getRedepositProductDetail(int redepositProductId);

    // 상품 가입 (계좌 생성)
    void openAccount(AccountCreationDto accountCreationDto, int userId, int depositProductId);

    // 가입한 상품 상세 조회
    AccountDetailResDto getMyAccountDetail(int depositAccountId, int userId);

    // 정기 예금 내역 조회
    AccountHistoryResDto getMyAccountTransfer(int depositAccountId, int id);

    // 정기 예금 만기해지
    void expireReDepositAccount();
}
