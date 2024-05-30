package com.ssafy.nhdream.domain.loan.dto;

import com.ssafy.nhdream.entity.loan.LoanApproval;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MyLoanDetailResDto {

    private int approval;
    private String name;
    private BigDecimal paidPrincipal;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal outstandingAmount;
    private LocalDate startedAt;
    private LocalDate expirationAt;
    private int term;
    private int round;
    private int paymentMethod;
    private int paymentDate;

}
