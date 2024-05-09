package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class LoanProductResDto {

    private String name;
    private BigDecimal amountRange;
    private BigDecimal farmerRate24;
    private BigDecimal farmerRate12;
    private BigDecimal farmerRate06;
    private BigDecimal trainRate24;
    private BigDecimal trainRate12;
    private BigDecimal trainRate06;



}
