package com.ssafy.nhdream.domain.saving.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SavingProductDetailResDto {
    private int id;
    private String name;
    private BigDecimal maxMonthlyLimit;

    //수료생 이자
    private BigDecimal graduateRate6Months;
    private BigDecimal graduateRate12Months;
    private BigDecimal graduateRate24Months;

    //귀농인 이자
    private BigDecimal farmerRate6Months;
    private BigDecimal farmerRate12Months;
    private BigDecimal farmerRate24Months;

    //최소,최대 이자율
    private BigDecimal minRate;
    private BigDecimal maxRate;

    //각 옵션의 옵션id
    private int option6Id;
    private int option12Id;
    private int option24Id;

    @Builder
    public SavingProductDetailResDto(int id, String name, BigDecimal maxMonthlyLimit,
                                   BigDecimal graduateRate6Months, BigDecimal graduateRate12Months, BigDecimal graduateRate24Months,
                                   BigDecimal farmerRate6Months, BigDecimal farmerRate12Months, BigDecimal farmerRate24Months,
                                   BigDecimal minRate, BigDecimal maxRate,
                                   int option6Id, int option12Id, int option24Id) {
        this.id = id;
        this.name = name;
        this.maxMonthlyLimit = maxMonthlyLimit;
        this.graduateRate6Months = graduateRate6Months;
        this.graduateRate12Months = graduateRate12Months;
        this.graduateRate24Months = graduateRate24Months;
        this.farmerRate6Months = farmerRate6Months;
        this.farmerRate12Months = farmerRate12Months;
        this.farmerRate24Months = farmerRate24Months;
        this.minRate = minRate;
        this.maxRate = maxRate;
        this.option6Id = option6Id;
        this.option12Id = option12Id;
        this.option24Id = option24Id;
    }
}
