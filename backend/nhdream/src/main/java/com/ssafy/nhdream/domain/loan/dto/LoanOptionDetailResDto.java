package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class LoanOptionDetailResDto {

    private String name;
    private LocalDate startedAt;
    private LocalDate expirationAt;
    private BigDecimal amount;
    private BigDecimal rate;
    private int paymentMethod;
    private int paymentDate;

}
