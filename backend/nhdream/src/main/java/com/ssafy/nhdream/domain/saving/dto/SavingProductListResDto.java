package com.ssafy.nhdream.domain.saving.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SavingProductListResDto {

    private int id;
    private String name;
    private BigDecimal maxInterestRate;
    private BigDecimal maxMonthlyLimit;

    @Builder
    public SavingProductListResDto(int id, String name, BigDecimal maxInterestRate, BigDecimal maxMonthlyLimit) {
        this.id = id;
        this.name = name;
        this.maxInterestRate = maxInterestRate;
        this.maxMonthlyLimit = maxMonthlyLimit;
    }
}
