package com.ssafy.nhdream.domain.user.service;

import com.ssafy.nhdream.domain.user.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    // 회원가입
    public int join(JoinReqDto joinReqDto);

    // 아이디 중복 검사
    void checkIdDuplicate(String id);

    // 교육 인증 신청
    void applyEducation(ApplyEducationReqDto applyEducationReqDto, MultipartFile educationImg);

    // 귀농 인증 신청
    void applyFarmer(ApplyFarmerReqDto applyFarmerReqDto, MultipartFile buisinessImg);

    //가입한 예적금 상품 조회
    List<GetMyAccountResDto> getMyAccounts(int userId);

    // 지갑비밀번호 검증
    void checkWalletPassword(VerifyWalletPasswordReqDto verifyWalletPasswordReqDto, int userId);

    // 지갑비밀번호 변경
    void changeWalletPassword(ChangeWalletPasswordReqDto changeWalletPasswordReqDto, int userId);

    // 로그인비밀번호 변경
    void changePassword(ChangePasswordReqDto changePasswordReqDto, int userId);

    // 개인 정보 조회
    UserInfoResDto getUserInfo(int userId);

    // 수료자 귀농 인증 신청
    void changeTypeToFarmer(int userId, ApplyFarmerReqDto applyFarmerReqDto, MultipartFile buisinessImg);
}
