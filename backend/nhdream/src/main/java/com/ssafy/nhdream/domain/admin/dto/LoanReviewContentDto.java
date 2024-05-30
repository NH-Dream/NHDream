package com.ssafy.nhdream.domain.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class LoanReviewContentDto {

    private int curReviewCnt;

    private int loanReviewId;

    private String userName;

    private String productName;

    private LocalDateTime applicationDate;

    private ApprovalStatus approvalStatus;

    private BigDecimal loanAmount;

    private BigDecimal interestRate;

    @QueryProjection
    public LoanReviewContentDto(int loanReviewId, String userName, String productName, LocalDateTime applicationDate, ApprovalStatus approvalStatus, BigDecimal loanAmount, BigDecimal interestRate) {
        this.loanReviewId = loanReviewId;
        this.userName = userName;
        this.productName = productName;
        this.applicationDate = applicationDate;
        this.approvalStatus = approvalStatus;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
    }
}
