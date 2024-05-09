package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class MyLoanListDto {

    private String name;
    private LocalDate date;
    private BigDecimal amount;
    private int approval;

}
