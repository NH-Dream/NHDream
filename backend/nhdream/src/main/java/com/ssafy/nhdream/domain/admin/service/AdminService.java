package com.ssafy.nhdream.domain.admin.service;

import com.ssafy.nhdream.domain.admin.dto.*;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    // 교육 인증 심사
    void reviewTraning(ReviewReqDto reviewReqDto);

    // 농부 인증 심사
    void reviewFarmer(ReviewReqDto reviewReqDto);

    // 대출 인증 심사
    void reviewLoan(LoanReviewReqDto loanReviewReqDto);

    // 대출 심사 목록 조회
    LoanReviewResDto getLoanReviewList(String userName, String status, Pageable pageable);

    // 대출 심사 상세 조회
    LoanReviewDetailResDto getLoanReviewDetail(int loanReviewId);

    // 지정된일자의 토큰발급,소각내역 불러오기
    List<NhTokenFlowDetailResDto> getNhTokenFlowResList(LocalDate startDate, LocalDate endDate);

    // 지정된일자의 NHDC 거래량 불러오기
    List<NhTokenTradingVolumeDetailResDto> getNhTokenTradeFlow(LocalDate startDate, LocalDate endDate);

//     소액지갑, 이자지갑, 지갑수 불러오기
    PersonalWalletCountResDto getPersonalWalletCount();

    // 교육 심사 목록 조회
    TrainingReviewResDto getTrainingReviewList(String userName, String status, Pageable pageable);

    // 귀농 심사 목록 조회
    FarmerReviewResDto getFarmerReviewList(String userName, Pageable pageable);

    // 귀농 인증 상세 조회
    FarmerReviewDetailResDto getFarmerReviewDetail(int farmerReviewId);

    // 수료생 인증 상세 조회
    TrainingReviewDetailResDto getTrainingReviewDetail(int trainingReviewId);

    //작업로그리스트조회
    List<LogListResDto> getLogList();

    //작업로그상세조회
    LogDetailResDto getLogDetail(int logId);
}
