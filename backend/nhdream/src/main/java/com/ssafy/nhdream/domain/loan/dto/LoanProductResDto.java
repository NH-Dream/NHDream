package com.ssafy.nhdream.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class LoanProductResDto {

    private String name;
    private BigDecimal amountRange;
    private BigDecimal minRate;
    private BigDecimal preferredRate1;
    private BigDecimal preferredRate2;


}
