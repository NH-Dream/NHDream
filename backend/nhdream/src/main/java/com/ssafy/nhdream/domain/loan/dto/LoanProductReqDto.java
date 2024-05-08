package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class LoanProductReqDto {

    private int id;
    private String name;
    private BigDecimal amountRange;
    private BigDecimal minRate;
    private BigDecimal maxRate;

}
