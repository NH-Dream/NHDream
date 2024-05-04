package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class LoanReqDto {

    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal outstanding;
    private LocalDate createdAt;
    private LocalDate expirationAt;
    // 납입회차 추가
    // 결제 방법 추가
    // 결제일 추가

}
