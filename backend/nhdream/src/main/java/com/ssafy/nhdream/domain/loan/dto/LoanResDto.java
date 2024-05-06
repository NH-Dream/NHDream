package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class LoanResDto {

    private int loanProductId;
    private BigDecimal amount;
    private LocalDate startedAt;
    private LocalDate expirationAt;
    private int term;
    private int userType;
    private int paymentMethod;
    private int paymentDate;
    private BigDecimal rate;

}
