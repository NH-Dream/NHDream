package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class LoanApprovalReqDto {

    // 회원 아이디, 대출 옵션 아이디, 신분증, 농지취득자격증명, 소득금액증명, 국세완납증명서, 지방세완납증명서
    private int userId;
    private int loanOptionsId;
    private MultipartFile identityUrl;
    private MultipartFile farmlandAccessUrl;
    private MultipartFile incomeCertificateUrl;
    private MultipartFile nationalTaxCertificateUrl;
    private MultipartFile localTaxCertificateUrl;

}
