package com.ssafy.nhdream.domain.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FarmerReviewContentDto {

    // 현재 컨텐츠 번호
    private int curReviewCnt;

    // 교육 심사 PK
    private int farmerReviewId;

    // 신청자명
    private String userName;

    // 사업자번호
    private long licenseNum;

    // 신청일자
    private LocalDateTime applicationDate;

    // 심사 상태
    private ApprovalStatus approvalStatus;

    @QueryProjection
    public FarmerReviewContentDto(int farmerReviewId, String userName, long licenseNum, LocalDateTime applicationDate, ApprovalStatus approvalStatus) {
        this.farmerReviewId = farmerReviewId;
        this.userName = userName;
        this.licenseNum = licenseNum;
        this.applicationDate = applicationDate;
        this.approvalStatus = approvalStatus;
    }
}
