package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class LoanResDto {

    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal outstanding;
    private LocalDate createdAt;
    private LocalDate expirationAt;
    private int round;
    private int paymentMethod;
    private int paymentDate;

}
